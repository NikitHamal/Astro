package com.astro.storm.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.unit.Density
import com.astro.storm.R
import com.astro.storm.data.localization.asString
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.ephemeris.AshtakavargaCalculator
import com.astro.storm.ephemeris.AspectCalculator
import com.astro.storm.ephemeris.DivisionalChartCalculator
import com.astro.storm.ephemeris.ShadbalaCalculator
import com.astro.storm.ephemeris.YogaCalculator
import com.astro.storm.ui.chart.ChartRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChartExporter(private val context: Context) {

    private val chartRenderer = ChartRenderer()

    companion object {
        private const val PDF_PAGE_WIDTH = 595 // A4 width in points (72 dpi)
        private const val PDF_PAGE_HEIGHT = 842 // A4 height in points
        private const val PDF_MARGIN = 40
        private const val CHART_SIZE = 400

        private const val WATERMARK_ALPHA = 80

        private val dateFormatter = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)
        private val displayDateFormatter = SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.US)
    }

    enum class ExportFormat { PDF, JSON, CSV, PNG, TEXT }

    sealed class ExportResult {
        data class Success(val path: String, val format: ExportFormat) : ExportResult()
        data class Error(val message: String) : ExportResult()
    }

    data class PdfExportOptions(
        val includeChart: Boolean = true,
        val includeNavamsa: Boolean = true,
        val includePlanetaryPositions: Boolean = true,
        val includeAspects: Boolean = true,
        val includeShadbala: Boolean = true,
        val includeYogas: Boolean = true,
        val includeAshtakavarga: Boolean = true,
        val pageSize: PageSize = PageSize.A4
    )

    enum class PageSize(val width: Int, val height: Int) { A4(595, 842), LETTER(612, 792) }

    data class ImageExportOptions(
        val width: Int = 2048,
        val height: Int = 2048,
        val addWatermark: Boolean = true,
        val watermarkText: String
    )

    suspend fun exportToPdf(chart: VedicChart, options: PdfExportOptions = PdfExportOptions(), density: Density): ExportResult = withContext(Dispatchers.IO) {
        try {
            val document = PdfDocument()
            var pageNumber = 1

            pageNumber = addChartPage(document, chart, options, density, pageNumber)
            if (options.includePlanetaryPositions) pageNumber = addPlanetaryPositionsPage(document, chart, options, pageNumber)
            if (options.includeAspects || options.includeYogas) pageNumber = addAspectsYogasPage(document, chart, options, pageNumber)
            if (options.includeShadbala) pageNumber = addShadbalaPage(document, chart, options, pageNumber)
            if (options.includeAshtakavarga) addAshtakavargaPage(document, chart, options, pageNumber)

            val fileName = "${context.getString(R.string.exporter_file_name_prefix)}_${chart.birthData.name.replace(" ", "_")}_${dateFormatter.format(Date())}.pdf"
            val path = saveDocument(document, fileName)
            document.close()
            ExportResult.Success(path, ExportFormat.PDF)
        } catch (e: Exception) {
            ExportResult.Error(context.getString(R.string.exporter_pdf_creation_error, e.message))
        }
    }

    private fun addChartPage(document: PdfDocument, chart: VedicChart, options: PdfExportOptions, density: Density, pageNumber: Int): Int {
        val page = document.startPage(PdfDocument.PageInfo.Builder(options.pageSize.width, options.pageSize.height, pageNumber).create())
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true; typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL) }
        var yPos = PDF_MARGIN.toFloat()

        paint.textSize = 24f; paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD); paint.color = Color.rgb(70, 70, 70)
        canvas.drawText(context.getString(R.string.pdf_report_title), PDF_MARGIN.toFloat(), yPos + 24f, paint)
        yPos += 50f

        paint.textSize = 12f; paint.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL); paint.color = Color.rgb(50, 50, 50)
        canvas.drawRect(PDF_MARGIN.toFloat(), yPos, (options.pageSize.width - PDF_MARGIN).toFloat(), yPos + 80f, Paint().apply { style = Paint.Style.STROKE; color = Color.rgb(180, 140, 100); strokeWidth = 1f })

        yPos += 15f
        canvas.drawText("${context.getString(R.string.pdf_birth_info_name)} ${chart.birthData.name}", PDF_MARGIN + 10f, yPos + 12f, paint)
        yPos += 18f
        canvas.drawText("${context.getString(R.string.pdf_birth_info_datetime)} ${chart.birthData.dateTime}", PDF_MARGIN + 10f, yPos + 12f, paint)
        yPos += 18f
        canvas.drawText("${context.getString(R.string.pdf_birth_info_location)} ${chart.birthData.location}", PDF_MARGIN + 10f, yPos + 12f, paint)
        yPos += 18f
        canvas.drawText("${context.getString(R.string.pdf_birth_info_coordinates)} ${formatCoordinate(chart.birthData.latitude.toDouble(), true)}, ${formatCoordinate(chart.birthData.longitude.toDouble(), false)}", PDF_MARGIN + 10f, yPos + 12f, paint)
        yPos += 40f

        if (options.includeChart) {
            val chartBitmap = chartRenderer.createChartBitmap(chart, CHART_SIZE, CHART_SIZE, density)
            canvas.drawBitmap(chartBitmap, (options.pageSize.width - CHART_SIZE) / 2f, yPos, null)
            yPos += CHART_SIZE + 20f
            paint.textSize = 10f; paint.textAlign = Paint.Align.CENTER
            canvas.drawText(context.getString(R.string.pdf_chart_label_rashi), options.pageSize.width / 2f, yPos, paint)
            paint.textAlign = Paint.Align.LEFT; yPos += 20f
        }

        if (options.includeNavamsa) {
            val navamsaData = DivisionalChartCalculator.calculateNavamsa(chart)
            val navamsaBitmap = chartRenderer.createDivisionalChartBitmap(navamsaData.planetPositions, navamsaData.ascendantLongitude, context.getString(R.string.pdf_chart_label_navamsa), 250, 250, density)
            canvas.drawBitmap(navamsaBitmap, (options.pageSize.width - 250) / 2f, yPos, null)
            yPos += 260f
            paint.textSize = 10f; paint.textAlign = Paint.Align.CENTER
            canvas.drawText(context.getString(R.string.pdf_chart_label_navamsa), options.pageSize.width / 2f, yPos, paint)
            paint.textAlign = Paint.Align.LEFT
        }

        addPageFooter(canvas, options.pageSize, pageNumber, paint)
        document.finishPage(page)
        return pageNumber + 1
    }

    private fun addPlanetaryPositionsPage(document: PdfDocument, chart: VedicChart, options: PdfExportOptions, pageNumber: Int): Int {
        val page = document.startPage(PdfDocument.PageInfo.Builder(options.pageSize.width, options.pageSize.height, pageNumber).create())
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true; typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL) }
        var yPos = PDF_MARGIN.toFloat()

        paint.textSize = 18f; paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD); paint.color = Color.rgb(70, 70, 70)
        canvas.drawText(context.getString(R.string.pdf_page_planetary_positions), PDF_MARGIN.toFloat(), yPos + 18f, paint)
        yPos += 40f

        paint.textSize = 10f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        val columns = listOf(context.getString(R.string.pdf_table_header_planet), context.getString(R.string.pdf_table_header_sign), context.getString(R.string.pdf_table_header_degree), context.getString(R.string.pdf_table_header_nakshatra), context.getString(R.string.pdf_table_header_pada), context.getString(R.string.pdf_table_header_house), context.getString(R.string.pdf_table_header_status))
        val columnWidths = listOf(60f, 80f, 70f, 100f, 40f, 50f, 80f)
        var xPos = PDF_MARGIN.toFloat()
        canvas.drawRect(PDF_MARGIN.toFloat(), yPos, (options.pageSize.width - PDF_MARGIN).toFloat(), yPos + 20f, Paint().apply { color = Color.rgb(240, 230, 210); style = Paint.Style.FILL })
        columns.forEachIndexed { index, column -> canvas.drawText(column, xPos + 5f, yPos + 14f, paint); xPos += columnWidths[index] }
        yPos += 25f

        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL); paint.textSize = 9f
        chart.planetPositions.forEach { position ->
            xPos = PDF_MARGIN.toFloat()
            val degreeInSign = position.longitude % 30.0
            val deg = degreeInSign.toInt(); val min = ((degreeInSign - deg) * 60).toInt(); val sec = ((((degreeInSign - deg) * 60) - min) * 60).toInt()
            val status = buildString {
                if (position.isRetrograde) append("${context.getString(R.string.pdf_planet_status_retrograde_short)} ")
                if (isExalted(position.planet, position.sign)) append("${context.getString(R.string.pdf_planet_status_exalted)} ")
                if (isDebilitated(position.planet, position.sign)) append("${context.getString(R.string.pdf_planet_status_debilitated)} ")
            }.trim().ifEmpty { "-" }
            val data = listOf(position.planet.asString(context), position.sign.asString(context), "$deg° $min' $sec\"", position.nakshatra.asString(context), position.nakshatraPada.toString(), position.house.toString(), status)
            data.forEachIndexed { index, value -> canvas.drawText(value, xPos + 5f, yPos + 12f, paint); xPos += columnWidths[index] }
            paint.color = Color.rgb(220, 220, 220); paint.strokeWidth = 0.5f
            canvas.drawLine(PDF_MARGIN.toFloat(), yPos + 18f, (options.pageSize.width - PDF_MARGIN).toFloat(), yPos + 18f, paint)
            paint.color = Color.rgb(50, 50, 50); yPos += 20f
        }

        yPos += 20f; paint.textSize = 14f; paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        canvas.drawText(context.getString(R.string.pdf_astro_data_title), PDF_MARGIN.toFloat(), yPos + 14f, paint)
        yPos += 30f; paint.textSize = 10f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        val astroData = listOf("${context.getString(R.string.pdf_astro_data_julian_day)}: ${String.format("%.6f", chart.julianDay)}", "${context.getString(R.string.pdf_astro_data_ayanamsa)}: ${chart.ayanamsaName}", "${context.getString(R.string.pdf_astro_data_ayanamsa)}: ${formatDegree(chart.ayanamsa)}", "${context.getString(R.string.pdf_astro_data_ascendant)}: ${formatDegree(chart.ascendant)} (${ZodiacSign.fromLongitude(chart.ascendant).asString(context)})", "${context.getString(R.string.pdf_astro_data_midheaven)}: ${formatDegree(chart.midheaven)} (${ZodiacSign.fromLongitude(chart.midheaven).asString(context)})", "${context.getString(R.string.pdf_astro_data_house_system)}: ${chart.houseSystem.asString(context)}")
        astroData.forEach { line -> canvas.drawText(line, PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 18f }

        yPos += 20f; paint.textSize = 14f; paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        canvas.drawText(context.getString(R.string.pdf_house_cusps_title), PDF_MARGIN.toFloat(), yPos + 14f, paint)
        yPos += 30f; paint.textSize = 9f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        chart.houseCusps.forEachIndexed { index, cusp ->
            val houseNum = index + 1
            val text = context.getString(R.string.pdf_house_cusps_house, houseNum) + ": ${formatDegree(cusp)} (${ZodiacSign.fromLongitude(cusp).abbreviation})"
            if (houseNum <= 6) canvas.drawText(text, PDF_MARGIN.toFloat(), yPos + 12f, paint) else canvas.drawText(text, options.pageSize.width / 2f, yPos - (6 * 18f) + 12f, paint)
            if (houseNum <= 6) yPos += 18f
        }

        addPageFooter(canvas, options.pageSize, pageNumber, paint)
        document.finishPage(page)
        return pageNumber + 1
    }

    private fun addAspectsYogasPage(document: PdfDocument, chart: VedicChart, options: PdfExportOptions, pageNumber: Int): Int {
        val page = document.startPage(PdfDocument.PageInfo.Builder(options.pageSize.width, options.pageSize.height, pageNumber).create())
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true; typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL) }
        var yPos = PDF_MARGIN.toFloat()

        if (options.includeYogas) {
            paint.textSize = 18f; paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD); paint.color = Color.rgb(70, 70, 70)
            canvas.drawText(context.getString(R.string.pdf_page_yogas_title), PDF_MARGIN.toFloat(), yPos + 18f, paint)
            yPos += 35f
            val yogaAnalysis = YogaCalculator.calculateYogas(chart)
            paint.textSize = 10f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            canvas.drawText("${context.getString(R.string.pdf_yogas_total)} ${yogaAnalysis.allYogas.size}", PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 18f
            canvas.drawText("${context.getString(R.string.pdf_yogas_overall_strength)} ${String.format("%.1f", yogaAnalysis.overallYogaStrength)}%", PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 25f
            val topYogas = yogaAnalysis.allYogas.filter { it.isAuspicious }.sortedByDescending { it.strengthPercentage }.take(10)
            if (topYogas.isNotEmpty()) {
                paint.textSize = 12f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                canvas.drawText(context.getString(R.string.pdf_yogas_key_yogas), PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 20f
                paint.textSize = 9f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                topYogas.forEach { yoga ->
                    val planets = yoga.planets.joinToString(", ") { it.asString(context) }
                    canvas.drawText("• ${yoga.name.asString(context)} ($planets)", PDF_MARGIN.toFloat() + 10f, yPos + 10f, paint); yPos += 14f
                    paint.color = Color.rgb(100, 100, 100)
                    val effects = yoga.effects.asString(context); val effectText = if (effects.length > 80) effects.substring(0, 77) + "..." else effects
                    canvas.drawText("  ${yoga.strength.asString(context)}: $effectText", PDF_MARGIN.toFloat() + 15f, yPos + 10f, paint)
                    paint.color = Color.rgb(50, 50, 50); yPos += 16f
                    if (yPos > options.pageSize.height - 100) return@forEach
                }
            }
            if (yogaAnalysis.negativeYogas.isNotEmpty() && yPos < options.pageSize.height - 150) {
                yPos += 15f; paint.textSize = 12f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD); paint.color = Color.rgb(150, 50, 50)
                canvas.drawText(context.getString(R.string.pdf_yogas_challenging_yogas), PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 20f
                paint.textSize = 9f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL); paint.color = Color.rgb(50, 50, 50)
                yogaAnalysis.negativeYogas.take(3).forEach { yoga ->
                    canvas.drawText("• ${yoga.name.asString(context)}", PDF_MARGIN.toFloat() + 10f, yPos + 10f, paint); yPos += 14f
                    if (yoga.cancellationFactors.isNotEmpty()) {
                        canvas.drawText("  ${context.getString(R.string.pdf_yogas_mitigated_by)} ${yoga.cancellationFactors.first().asString(context)}", PDF_MARGIN.toFloat() + 15f, yPos + 10f, paint); yPos += 14f
                    }
                }
            }
        }

        if (options.includeAspects && yPos < options.pageSize.height - 200) {
            yPos += 25f; paint.textSize = 18f; paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD); paint.color = Color.rgb(70, 70, 70)
            canvas.drawText(context.getString(R.string.pdf_page_aspects_title), PDF_MARGIN.toFloat(), yPos + 18f, paint); yPos += 35f
            val significantAspects = AspectCalculator.calculateAspectMatrix(chart).aspects.filter { it.drishtiBala > 0.5 }.sortedByDescending { it.drishtiBala }.take(15)
            paint.textSize = 9f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            significantAspects.forEach { aspect ->
                val applying = if (aspect.isApplying) context.getString(R.string.pdf_aspect_applying) else context.getString(R.string.pdf_aspect_separating)
                val text = "${aspect.aspectingPlanet.asString(context)} ${aspect.aspectType.asString(context)} ${aspect.aspectedPlanet.asString(context)} (${context.getString(R.string.pdf_aspect_orb, aspect.exactOrb)}, $applying)"
                canvas.drawText(text, PDF_MARGIN.toFloat(), yPos + 10f, paint); yPos += 14f
                if (yPos > options.pageSize.height - 80) return@forEach
            }
        }

        addPageFooter(canvas, options.pageSize, pageNumber, paint)
        document.finishPage(page)
        return pageNumber + 1
    }

    private fun addShadbalaPage(document: PdfDocument, chart: VedicChart, options: PdfExportOptions, pageNumber: Int): Int {
        val page = document.startPage(PdfDocument.PageInfo.Builder(options.pageSize.width, options.pageSize.height, pageNumber).create())
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true; typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL) }
        var yPos = PDF_MARGIN.toFloat()

        paint.textSize = 18f; paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD); paint.color = Color.rgb(70, 70, 70)
        canvas.drawText(context.getString(R.string.pdf_page_shadbala_title), PDF_MARGIN.toFloat(), yPos + 18f, paint); yPos += 40f
        val shadbala = ShadbalaCalculator.calculateShadbala(chart)
        paint.textSize = 11f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        canvas.drawText("${context.getString(R.string.pdf_shadbala_overall_strength)} ${String.format("%.1f", shadbala.overallStrengthScore)}%", PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 20f
        canvas.drawText("${context.getString(R.string.pdf_shadbala_strongest_planet)} ${shadbala.strongestPlanet.asString(context)}", PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 20f
        canvas.drawText("${context.getString(R.string.pdf_shadbala_weakest_planet)} ${shadbala.weakestPlanet.asString(context)}", PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 35f

        paint.textSize = 10f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        val columns = listOf(context.getString(R.string.pdf_table_header_planet), context.getString(R.string.pdf_shadbala_table_total_rupas), context.getString(R.string.pdf_shadbala_table_required), context.getString(R.string.pdf_shadbala_table_percent), context.getString(R.string.pdf_shadbala_table_rating))
        val columnWidths = listOf(80f, 80f, 70f, 60f, 120f)
        var xPos = PDF_MARGIN.toFloat()
        canvas.drawRect(PDF_MARGIN.toFloat(), yPos, (options.pageSize.width - PDF_MARGIN).toFloat(), yPos + 22f, Paint().apply { color = Color.rgb(240, 230, 210); style = Paint.Style.FILL })
        columns.forEachIndexed { index, column -> canvas.drawText(column, xPos + 5f, yPos + 16f, paint); xPos += columnWidths[index] }
        yPos += 28f

        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        shadbala.getPlanetsByStrength().forEach { planetShadbala ->
            xPos = PDF_MARGIN.toFloat()
            val data = listOf(planetShadbala.planet.asString(context), String.format("%.2f", planetShadbala.totalRupas), String.format("%.2f", planetShadbala.requiredRupas), String.format("%.0f%%", planetShadbala.percentageOfRequired), planetShadbala.strengthRating.asString(context))
            paint.color = when { planetShadbala.percentageOfRequired >= 100 -> Color.rgb(0, 100, 0); planetShadbala.percentageOfRequired >= 80 -> Color.rgb(50, 50, 50); else -> Color.rgb(150, 50, 50) }
            data.forEachIndexed { index, value -> canvas.drawText(value, xPos + 5f, yPos + 14f, paint); xPos += columnWidths[index] }
            paint.color = Color.rgb(220, 220, 220); canvas.drawLine(PDF_MARGIN.toFloat(), yPos + 20f, (options.pageSize.width - PDF_MARGIN).toFloat(), yPos + 20f, paint)
            paint.color = Color.rgb(50, 50, 50); yPos += 24f
        }

        yPos += 25f
        val strongest = shadbala.planetaryStrengths[shadbala.strongestPlanet]
        if (strongest != null) {
            paint.textSize = 12f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            canvas.drawText(context.getString(R.string.pdf_shadbala_breakdown_title, strongest.planet.asString(context)), PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 25f
            paint.textSize = 9f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            val breakdown = listOf(context.getString(R.string.shadbala_sthana_bala) + ": ${String.format("%.1f", strongest.sthanaBala.total)} ${context.getString(R.string.pdf_shadbala_virupas)}", context.getString(R.string.shadbala_dig_bala) + ": ${String.format("%.1f", strongest.digBala)} ${context.getString(R.string.pdf_shadbala_virupas)}", context.getString(R.string.shadbala_kala_bala) + ": ${String.format("%.1f", strongest.kalaBala.total)} ${context.getString(R.string.pdf_shadbala_virupas)}", context.getString(R.string.shadbala_chesta_bala) + ": ${String.format("%.1f", strongest.chestaBala)} ${context.getString(R.string.pdf_shadbala_virupas)}", context.getString(R.string.shadbala_naisargika_bala) + ": ${String.format("%.1f", strongest.naisargikaBala)} ${context.getString(R.string.pdf_shadbala_virupas)}", context.getString(R.string.shadbala_drik_bala) + ": ${String.format("%.1f", strongest.drikBala)} ${context.getString(R.string.pdf_shadbala_virupas)}")
            breakdown.forEach { line -> canvas.drawText(line, PDF_MARGIN.toFloat() + 15f, yPos + 10f, paint); yPos += 16f }
        }

        addPageFooter(canvas, options.pageSize, pageNumber, paint)
        document.finishPage(page)
        return pageNumber + 1
    }

    private fun addAshtakavargaPage(document: PdfDocument, chart: VedicChart, options: PdfExportOptions, pageNumber: Int): Int {
        val page = document.startPage(PdfDocument.PageInfo.Builder(options.pageSize.width, options.pageSize.height, pageNumber).create())
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true; typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL) }
        var yPos = PDF_MARGIN.toFloat()

        paint.textSize = 18f; paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD); paint.color = Color.rgb(70, 70, 70)
        canvas.drawText(context.getString(R.string.pdf_page_ashtakavarga_title), PDF_MARGIN.toFloat(), yPos + 18f, paint); yPos += 40f
        val ashtakavarga = AshtakavargaCalculator.calculateAshtakavarga(chart)
        paint.textSize = 12f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        canvas.drawText(context.getString(R.string.pdf_ashtakavarga_sav_title), PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 25f
        paint.textSize = 9f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

        val signWidth = 45f; var xPos = PDF_MARGIN.toFloat() + 30f; paint.textSize = 8f
        ZodiacSign.entries.forEach { sign -> canvas.drawText(sign.abbreviation, xPos, yPos + 10f, paint); xPos += signWidth }
        yPos += 20f; xPos = PDF_MARGIN.toFloat(); canvas.drawText("SAV:", xPos, yPos + 10f, paint); xPos += 30f
        ZodiacSign.entries.forEach { sign ->
            val bindus = ashtakavarga.sarvashtakavarga.getBindusForSign(sign)
            paint.color = when { bindus >= 30 -> Color.rgb(0, 100, 0); bindus >= 25 -> Color.rgb(50, 50, 50); else -> Color.rgb(150, 50, 50) }
            canvas.drawText(bindus.toString(), xPos, yPos + 10f, paint); xPos += signWidth
        }
        paint.color = Color.rgb(50, 50, 50); yPos += 25f

        paint.textSize = 10f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        canvas.drawText(context.getString(R.string.pdf_ashtakavarga_bav_title), PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 25f
        paint.textSize = 8f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        ashtakavarga.bhinnashtakavarga.forEach { (planet, bav) ->
            xPos = PDF_MARGIN.toFloat(); canvas.drawText(planet.symbol + ":", xPos, yPos + 10f, paint); xPos += 30f
            ZodiacSign.entries.forEach { sign -> canvas.drawText(bav.getBindusForSign(sign).toString(), xPos, yPos + 10f, paint); xPos += signWidth }
            canvas.drawText("=${bav.totalBindus}", xPos, yPos + 10f, paint); yPos += 16f
        }

        yPos += 20f; paint.textSize = 11f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        canvas.drawText(context.getString(R.string.pdf_ashtakavarga_transit_guide_title), PDF_MARGIN.toFloat(), yPos + 12f, paint); yPos += 22f
        paint.textSize = 9f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        val guide = listOf(context.getString(R.string.pdf_ashtakavarga_sav_excellent), context.getString(R.string.pdf_ashtakavarga_sav_good), context.getString(R.string.pdf_ashtakavarga_sav_average), context.getString(R.string.pdf_ashtakavarga_sav_challenging), "", context.getString(R.string.pdf_ashtakavarga_bav_excellent), context.getString(R.string.pdf_ashtakavarga_bav_good), context.getString(R.string.pdf_ashtakavarga_bav_average), context.getString(R.string.pdf_ashtakavarga_bav_challenging))
        guide.forEach { line -> canvas.drawText(line, PDF_MARGIN.toFloat() + 10f, yPos + 10f, paint); yPos += 14f }

        addPageFooter(canvas, options.pageSize, pageNumber, paint)
        document.finishPage(page)
        return pageNumber + 1
    }

    private fun addPageFooter(canvas: Canvas, pageSize: PageSize, pageNumber: Int, paint: Paint) {
        paint.textSize = 8f; paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL); paint.color = Color.rgb(128, 128, 128)
        val footerY = pageSize.height - 25f
        canvas.drawText(context.getString(R.string.pdf_footer_generated_by), PDF_MARGIN.toFloat(), footerY, paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(context.getString(R.string.pdf_footer_page, pageNumber), (pageSize.width - PDF_MARGIN).toFloat(), footerY, paint)
        paint.textAlign = Paint.Align.LEFT
    }

    private fun saveDocument(document: PdfDocument, fileName: String): String {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/AstroStorm")
            }
        }
        val uri = resolver.insert(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Files.getContentUri("external") else MediaStore.Files.getContentUri("external"), contentValues) ?: throw Exception(context.getString(R.string.exporter_file_creation_failed))
        resolver.openOutputStream(uri)?.use { document.writeTo(it) }
        return uri.toString()
    }


    suspend fun exportToJson(chart: VedicChart): ExportResult = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("birthData", JSONObject().apply {
                    put("name", chart.birthData.name)
                    put("dateTime", chart.birthData.dateTime.toString())
                    put("latitude", chart.birthData.latitude)
                    put("longitude", chart.birthData.longitude)
                    put("timezone", chart.birthData.timezone)
                    put("location", chart.birthData.location)
                })
                put("astronomicalData", JSONObject().apply {
                    put("julianDay", chart.julianDay)
                    put("ayanamsa", chart.ayanamsa)
                    put("ayanamsaName", chart.ayanamsaName)
                    put("ascendant", chart.ascendant)
                    put("ascendantSign", ZodiacSign.fromLongitude(chart.ascendant).name)
                    put("midheaven", chart.midheaven)
                    put("midheavenSign", ZodiacSign.fromLongitude(chart.midheaven).name)
                    put("houseSystem", chart.houseSystem.name)
                })
                put("planetaryPositions", JSONArray(chart.planetPositions.map { pos ->
                    JSONObject().apply {
                        put("planet", pos.planet.name)
                        put("longitude", pos.longitude)
                        put("sign", pos.sign.name)
                        put("signDisplayName", pos.sign.asString(context))
                        put("nakshatra", pos.nakshatra.name)
                        put("nakshatraDisplayName", pos.nakshatra.asString(context))
                        put("nakshatraPada", pos.nakshatraPada)
                        put("isRetrograde", pos.isRetrograde)
                    }
                }))
                put("metadata", JSONObject().apply {
                    put("generatedAt", System.currentTimeMillis())
                    put("generatedBy", context.getString(R.string.json_metadata_generated_by))
                    put("calculationEngine", context.getString(R.string.json_metadata_engine))
                })
            }
            val fileName = "${context.getString(R.string.exporter_file_name_prefix)}_${chart.birthData.name.replace(" ", "_")}_${dateFormatter.format(Date())}.json"
            val path = saveTextToFile(json.toString(2), fileName, "application/json")
            ExportResult.Success(path, ExportFormat.JSON)
        } catch (e: Exception) {
            ExportResult.Error(context.getString(R.string.exporter_json_creation_error, e.message))
        }
    }


    suspend fun exportToCsv(chart: VedicChart): ExportResult = withContext(Dispatchers.IO) {
        try {
            val csvBuilder = StringBuilder().apply {
                appendLine(context.getString(R.string.csv_header_planetary_positions))
                appendLine(context.getString(R.string.csv_header_planetary_positions_cols))
                chart.planetPositions.forEach { pos ->
                    appendLine("${pos.planet.asString(context)},${pos.longitude},${pos.sign.asString(context)},${pos.longitude % 30.0},${pos.house},${pos.nakshatra.asString(context)},${pos.nakshatraPada},${pos.isRetrograde},${pos.speed}")
                }
                appendLine()
            }
            val fileName = "${context.getString(R.string.exporter_file_name_prefix)}_${chart.birthData.name.replace(" ", "_")}_${dateFormatter.format(Date())}.csv"
            val path = saveTextToFile(csvBuilder.toString(), fileName, "text/csv")
            ExportResult.Success(path, ExportFormat.CSV)
        } catch (e: Exception) {
            ExportResult.Error(context.getString(R.string.exporter_csv_creation_error, e.message))
        }
    }

    suspend fun exportToImage(chart: VedicChart, options: ImageExportOptions, density: Density): ExportResult = withContext(Dispatchers.IO) {
        try {
            var bitmap = chartRenderer.createChartBitmap(chart, options.width, options.height, density)
            if (options.addWatermark) bitmap = addWatermark(bitmap, options.watermarkText)
            addTitle(bitmap, chart)
            val fileName = "${context.getString(R.string.exporter_file_name_prefix)}_${chart.birthData.name.replace(" ", "_")}_${dateFormatter.format(Date())}.png"
            val path = saveImageFile(bitmap, fileName)
            ExportResult.Success(path, ExportFormat.PNG)
        } catch (e: Exception) {
            ExportResult.Error(context.getString(R.string.exporter_image_creation_error, e.message))
        }
    }

    private fun addWatermark(bitmap: Bitmap, watermarkText: String): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint().apply { color = Color.argb(WATERMARK_ALPHA, 128, 128, 128); textSize = bitmap.width / 15f; typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC); isAntiAlias = true; textAlign = Paint.Align.CENTER }
        canvas.save()
        canvas.rotate(-30f, bitmap.width / 2f, bitmap.height / 2f)
        canvas.drawText(watermarkText, bitmap.width / 2f, bitmap.height / 2f, paint)
        canvas.restore()
        return result
    }

    private fun addTitle(bitmap: Bitmap, chart: VedicChart): Bitmap {
        val titleHeight = 80
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height + titleHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawRect(0f, 0f, bitmap.width.toFloat(), titleHeight.toFloat(), Paint().apply { color = Color.rgb(212, 196, 168); style = Paint.Style.FILL })
        val textPaint = Paint().apply { color = Color.rgb(70, 70, 70); textSize = 24f; typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD); isAntiAlias = true; textAlign = Paint.Align.CENTER }
        canvas.drawText(chart.birthData.name, bitmap.width / 2f, 30f, textPaint)
        textPaint.textSize = 14f; textPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
        canvas.drawText("${chart.birthData.dateTime} ${context.getString(R.string.image_title_separator)} ${chart.birthData.location}", bitmap.width / 2f, 55f, textPaint)
        canvas.drawBitmap(bitmap, 0f, titleHeight.toFloat(), null)
        return result
    }

    private fun saveImageFile(bitmap: Bitmap, fileName: String): String {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AstroStorm")
            }
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: throw Exception(context.getString(R.string.exporter_file_creation_failed))
        resolver.openOutputStream(uri)?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        return uri.toString()
    }


    suspend fun exportToText(chart: VedicChart): ExportResult = withContext(Dispatchers.IO) {
        try {
            val report = buildString {
                appendLine("══════════════════════════════════════════════════════")
                appendLine("          ${context.getString(R.string.text_report_header)}")
                appendLine("       ${context.getString(R.string.text_report_generated_by_short)}")
                appendLine("══════════════════════════════════════════════════════")
                appendLine()
                appendLine("${context.getString(R.string.text_report_generated_at)}: ${displayDateFormatter.format(Date())}")
                appendLine()

                appendLine(context.getString(R.string.text_report_birth_info))
                appendLine("──────────────────────────────────────────────────────")
                appendLine("${context.getString(R.string.text_report_birth_name)} ${chart.birthData.name}")
                appendLine("${context.getString(R.string.text_report_birth_datetime)} ${chart.birthData.dateTime}")
                appendLine("${context.getString(R.string.text_report_birth_location)} ${chart.birthData.location}")
                appendLine("${context.getString(R.string.text_report_birth_coords)} ${formatCoordinate(chart.birthData.latitude.toDouble(), true)}, ${formatCoordinate(chart.birthData.longitude.toDouble(), false)}")
                appendLine("${context.getString(R.string.text_report_birth_tz)} ${chart.birthData.timezone}")
                appendLine()

                appendLine(context.getString(R.string.text_report_chart_summary))
                appendLine("──────────────────────────────────────────────────────")
                val ascSign = ZodiacSign.fromLongitude(chart.ascendant)
                val moonPos = chart.planetPositions.find { it.planet == Planet.MOON }
                val sunPos = chart.planetPositions.find { it.planet == Planet.SUN }
                appendLine("${context.getString(R.string.text_report_summary_ascendant)} ${ascSign.asString(context)}")
                moonPos?.let { appendLine("${context.getString(R.string.text_report_summary_moon_sign)} ${it.sign.asString(context)}") }
                sunPos?.let { appendLine("${context.getString(R.string.text_report_summary_sun_sign)} ${it.sign.asString(context)}") }
                moonPos?.let { appendLine("${context.getString(R.string.text_report_summary_nakshatra)} ${it.nakshatra.asString(context)} (${context.getString(R.string.text_report_nakshatra_pada, it.nakshatraPada)})") }
                appendLine()

                val yogaAnalysis = YogaCalculator.calculateYogas(chart)
                appendLine(context.getString(R.string.pdf_page_yogas_title).uppercase(Locale.ROOT))
                appendLine("──────────────────────────────────────────────────────")
                yogaAnalysis.allYogas.sortedByDescending { it.strengthPercentage }.take(10).forEach { yoga ->
                    appendLine("• ${yoga.name.asString(context)} (${yoga.strength.asString(context)}, ${yoga.strengthPercentage.toInt()}%)")
                    appendLine("  ${yoga.effects.asString(context)}")
                }
                appendLine()

                appendLine("══════════════════════════════════════════════════════")
                appendLine("     ${context.getString(R.string.text_report_footer_precision)}")
                appendLine("     ${context.getString(R.string.text_report_footer_engine)}")
                appendLine("══════════════════════════════════════════════════════")
            }

            val fileName = "${context.getString(R.string.exporter_file_name_prefix)}_${chart.birthData.name.replace(" ", "_")}_${dateFormatter.format(Date())}.txt"
            val path = saveTextToFile(report, fileName, "text/plain")
            ExportResult.Success(path, ExportFormat.TEXT)
        } catch (e: Exception) {
            ExportResult.Error(context.getString(R.string.exporter_text_creation_error, e.message))
        }
    }


    private fun saveTextToFile(content: String, fileName: String, mimeType: String): String {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/AstroStorm")
            }
        }
        val uri = resolver.insert(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Files.getContentUri("external") else MediaStore.Files.getContentUri("external"), contentValues) ?: throw Exception(context.getString(R.string.exporter_file_creation_failed))
        resolver.openOutputStream(uri)?.use { it.write(content.toByteArray()) }
        return uri.toString()
    }

    private fun formatCoordinate(value: Double, isLatitude: Boolean): String {
        val abs = kotlin.math.abs(value)
        val degrees = abs.toInt()
        val minutes = ((abs - degrees) * 60).toInt()
        val seconds = ((((abs - degrees) * 60) - minutes) * 60).toInt()
        val direction = if (isLatitude) (if (value >= 0) "N" else "S") else (if (value >= 0) "E" else "W")
        return "$degrees° $minutes' $seconds\" $direction"
    }

    private fun formatDegree(degree: Double): String {
        val normalizedDegree = (degree % 360.0 + 360.0) % 360.0
        val deg = normalizedDegree.toInt()
        val min = ((normalizedDegree - deg) * 60).toInt()
        val sec = ((((normalizedDegree - deg) * 60) - min) * 60).toInt()
        return "$deg° $min' $sec\""
    }

    private fun isExalted(planet: Planet, sign: ZodiacSign): Boolean = when (planet) {
        Planet.SUN -> sign == ZodiacSign.ARIES; Planet.MOON -> sign == ZodiacSign.TAURUS
        Planet.MARS -> sign == ZodiacSign.CAPRICORN; Planet.MERCURY -> sign == ZodiacSign.VIRGO
        Planet.JUPITER -> sign == ZodiacSign.CANCER; Planet.VENUS -> sign == ZodiacSign.PISCES
        Planet.SATURN -> sign == ZodiacSign.LIBRA; else -> false
    }

    private fun isDebilitated(planet: Planet, sign: ZodiacSign): Boolean = when (planet) {
        Planet.SUN -> sign == ZodiacSign.LIBRA; Planet.MOON -> sign == ZodiacSign.SCORPIO
        Planet.MARS -> sign == ZodiacSign.CANCER; Planet.MERCURY -> sign == ZodiacSign.PISCES
        Planet.JUPITER -> sign == ZodiacSign.CAPRICORN; Planet.VENUS -> sign == ZodiacSign.VIRGO
        Planet.SATURN -> sign == ZodiacSign.ARIES; else -> false
    }
}
