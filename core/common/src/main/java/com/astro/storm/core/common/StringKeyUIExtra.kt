package com.astro.storm.core.common

/**
 * Modular String Keys for Technical UI Elements and Punctuation
 *
 * Separated to avoid exceeding JVM method size limits and to keep
 * technical formatting keys organized.
 */
enum class StringKeyUIExtra(override val en: String, override val ne: String) : StringKeyInterface {
    // Technical Punctuation & Symbols
    COLON_SPACE(": ", ": "),
    DASH_SPACE(" - ", " - "),
    PIPE_SPACE(" | ", " | "),
    COMMA_SPACE(", ", ", "),
    ARROW(" → ", " → "),
    ARROW_RIGHT("→", "→"),
    BULLET("•", "•"),
    BULLET_SPACE("• ", "• "),
    CHECKMARK_SPACE("✓ ", "✓ "),
    WARNING_SPACE("⚠ ", "⚠ "),
    OPEN_CIRCLE("○", "○"),
    CLOSED_CIRCLE("●", "●"),
    PERCENT("%", "%"),
    PAREN_START(" (", " ("),
    PAREN_END(")", ")"),
    BRACKET_START("[", "["),
    BRACKET_END("]", "]"),
    PIPE("|", "|"),
    SLASH("/", "/"),
    ELLIPSIS("...", "..."),
    DEGREE("°", "°"),
    ARC_MINUTE("'", "'"),
    ARC_SECOND("\"", "\""),
    BULLET_MD(" • ", " • "),
    CHECK_MARK("✓", "✓"),
    WARNING_SIGN("⚠", "⚠"),
    PERCENT_SIGN("%", "%"),
    RUPAS_SUFFIX(" R", " रु"),
    DATE_PATTERN_ISO("yyyy-MM-dd", "yyyy-MM-dd"),
    COMMA_SEP(", ", ", "),
    PIPE_SEP(" | ", " | "),
    ENERGY_SCORE_FMT("%d/10", "%d/१०"),
    PLANETS_COUNT_A11Y("%d planets %s", "%d ग्रहहरू %s"),
    BINDU_PLUS("Bindu +", "बिन्दु +"),
    BINDU_MINUS("Bindu -", "बिन्दु -"),
    IN_HOURS_FMT("in %dh", "%d घ मा"),
    IN_KAKSHYA_FMT("in %s Kakshya", "%s कक्ष्यामा"),
    X_MARK("✗", "✗"),

    // Technical Labels
    BIRTH_1ST_YR("Birth (1st Yr)", "जन्म (प्रथम वर्ष)"),
    HOUSE_PREFIX_SHORT("H", "भा"),
    TIMEZONES_COUNT_FMT("%d timezones", "%s समयक्षेत्रहरू"),
    LABEL_NO("No", "होइन"),
    LABEL_YES("Yes", "हो"),

    // Ayurvedic Doshas
    DOSHA_VATA("Vata (वात)", "वात"),
    DOSHA_PITTA("Pitta (पित्त)", "पित्त"),
    DOSHA_KAPHA("Kapha (कफ)", "कफ"),

    // Directions
    DIR_N("N", "उ"),
    DIR_NE("NE", "ई"),
    DIR_E("E", "पू"),
    DIR_SE("SE", "आ"),
    DIR_S("S", "द"),
    DIR_SW("SW", "नै"),
    DIR_W("W", "प"),
    DIR_NW("NW", "वा"),

    // Ordinals
    ORDINAL_ST("st", "औं"),
    ORDINAL_ND("nd", "औं"),
    ORDINAL_RD("rd", "औं"),
    ORDINAL_TH("th", "औं"),
    SYMBOL_SUCCESS("✅", "✅"),
    SYMBOL_WARNING("⚠️", "⚠️"),
    EMOJI_BRIDE("👰", "👰"),
    EMOJI_GROOM("🤵", "🤵"),
    EMOJI_REPORT("🔮", "🔮"),
    EMOJI_STAR("⭐", "⭐"),
    EMOJI_CHART("📊", "📊"),
    EMOJI_ALERT("🔴", "🔴"),

    // Badhaka Explanations
    BADHAKA_EXPL_MOVABLE("For Movable (Chara) signs, the 11th house is Badhaka Sthana", "चर राशिको लागि ११औं भाव बाधक स्थान हो"),
    BADHAKA_EXPL_FIXED("For Fixed (Sthira) signs, the 9th house is Badhaka Sthana", "स्थिर राशिको लागि ९औं भाव बाधक स्थान हो"),
    BADHAKA_EXPL_DUAL("For Dual (Dvisvabhava) signs, the 7th house is Badhaka Sthana", "द्विस्वभाव राशिको लागि ७औं भाव बाधक स्थान हो"),

    // Labels with trailing colon
    LABEL_STRENGTH_COLON("Strength: ", "बल: "),
    LABEL_FROM_COLON("from ", "बाट "),

    // Argala specific
    ARGALA_TYPES_TITLE("Argala Types", "अर्गला प्रकारहरू"),
    ARGALA_STRENGTH_LABEL("Argala Strength", "अर्गला बल"),
    ARGALA_HOUSE_PREFIX("House %d", "भाव %d"),
    ARGALA_EFFECTIVE("Effective", "प्रभावी"),
    ARGALA_OBSTRUCTION_LABEL("Obstruction", "अवरोध/बाधा"),
    ARGALA_BENEFIC_STRENGTH("Benefic Strength", "शुभ बल"),
    ARGALA_MALEFIC_STRENGTH("Malefic Strength", "अशुभ बल"),
    ARGALA_NET_STRENGTH_FMT("Net Strength: %s", "कुल बल: %s"),
    ARGALA_SOURCE("Source", "स्रोत"),
    ARGALA_KEY_CONCEPTS("Key Concepts", "मुख्य धारणाहरू"),
    ARGALA_CONCEPT_1("Argala refers to the 'intervention' or 'bolt' that one house/planet exercises over another.", "अर्गलाले एउटा भाव वा ग्रहले अर्कोमा गर्ने 'हस्तक्षेप' वा 'कुञ्जी' लाई जनाउँछ।"),
    ARGALA_CONCEPT_2("Planets in the 2nd, 4th, 11th, and 5th houses from a reference point create Argala.", "सन्दर्भ बिन्दुबाट दोस्रो, चौथो, एघारौं र पाँचौं भावमा रहेका ग्रहहरूले अर्गला सिर्जना गर्छन्।"),
    ARGALA_CONCEPT_3("Virodha Argala refers to the obstruction of an Argala by planets in opposing houses.", "विरोध अर्गलाले विपरित भावमा रहेका ग्रहहरूद्वारा अर्गलाको अवरोधलाई जनाउँछ।"),
    ARGALA_CONCEPT_4("Benefic Argala (Shubha) helps the significations of the house flourish.", "शुभ अर्गलाले भावको कारक तत्वहरूलाई फस्टाउन मद्दत गर्दछ।"),
    ARGALA_CONCEPT_5("Malefic Argala (Papa) creates challenges or pressures on the house.", "पाप अर्गलाले भावमा चुनौती वा दबाब सिर्जना गर्दछ।"),
    ARGALA_CONCEPT_6("A balanced analysis requires looking at both the intervening and obstructing planets.", "सन्तुलित विश्लेषणको लागि हस्तक्षेप गर्ने र अवरोध गर्ने दुवै ग्रहहरूलाई हेर्नु आवश्यक छ।"),
    ARGALA_INFLUENCES_RECEIVED("Influences Received", "प्राप्त प्रभावहरू"),
    ARGALA_OBSTRUCTIONS_RECEIVED("Obstructions Received", "प्राप्त अवरोधहरू"),

    // Remedies specific
    REMEDIES_UNKNOWN_ERROR("An unknown error occurred while calculating remedies.", "उपचार गणना गर्दा एउटा अज्ञात त्रुटि भयो।"),
    REMEDIES_TAB_A11Y("Select %s tab", "%s ट्याब चयन गर्नुहोस्"),
    REMEDIES_REQUIRES_ATTENTION("%s requires attention", "%s लाई ध्यान दिनु आवश्यक छ"),

    // Dasha Sandhi specific
    SANDHI_ABOUT_TITLE("About Dasha Sandhi", "दशा सन्धि परिचय"),
    SANDHI_ABOUT_DESC("Dasha Sandhi is the transition period between two major planetary periods (Mahadashas).", "दशा सन्धि दुई महादशाहरु बीचको संक्रमणकालीन अवधि हो।"),
    SANDHI_DESC_P1("Primary Phase:", "प्राथमिक चरण:"),
    SANDHI_DESC_P1_ITEM1("- Transition between major periods.", "- मुख्य अवधिहरू बीचको संक्रमण।"),
    SANDHI_DESC_P1_ITEM2("- High emotional volatility.", "- उच्च भावनात्मक अस्थिरता।"),
    SANDHI_DESC_P1_ITEM3("- Career shifts are common.", "- करियरमा परिवर्तन सामान्य हुन्छ।"),
    SANDHI_DESC_P1_ITEM4("- Health needs monitoring.", "- स्वास्थ्य निगरानी आवश्यक छ।"),
    SANDHI_DESC_P2("Secondary Phase:", "माध्यमिक चरण:"),
    SANDHI_DESC_P2_ITEM1("- Refinement of results.", "- परिणामहरूको परिमार्जन।"),
    SANDHI_DESC_P2_ITEM2("- New opportunities emerge.", "- नयाँ अवसरहरू देखा पर्छन्।"),
    SANDHI_DESC_P2_ITEM3("- Stabilizing influences.", "- स्थिर प्रभावहरू।"),
    SANDHI_DESC_FOOTER("Always consult with a qualified astrologer for major life decisions.", "मुख्य जीवन निर्णयहरूको लागि सधैं योग्य ज्योतिषीसँग परामर्श गर्नुहोस्।"),
    SANDHI_ANALYZING("Analyzing Dasha Sandhi...", "दशा सन्धि विश्लेषण गर्दै..."),
    SANDHI_VOL_VERY_HIGH("Very High", "धेरै उच्च"),
    SANDHI_VOL_HIGH("High", "उच्च"),
    SANDHI_VOL_MODERATE("Moderate", "मध्यम"),
    SANDHI_VOL_LOW("Low", "कम"),
    SANDHI_VOL_MINIMAL("Minimal", "न्यूनतम"),

    // Shadbala specific
    SHADBALA_SIXFOLD_COMP_TITLE("Six-fold Strength Comparison", "षडबल तुलना"),
}
