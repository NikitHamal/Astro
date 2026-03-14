package com.astro.vajra.ephemeris.dsl

import com.astro.vajra.core.model.Nakshatra
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.VedicAstrologyUtils
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.abs

/**
 * Astro-DSL: Domain-Specific Language for Custom Yoga Definition and Evaluation
 *
 * This module implements a complete DSL (Domain-Specific Language) that allows users to define
 * custom Vedic astrology yoga combinations using either a human-readable text syntax or JSON
 * format. The DSL is compiled into an Abstract Syntax Tree (AST) and evaluated against a
 * VedicChart to detect custom yogas.
 *
 * **Architecture:**
 * 1. **Lexer/Tokenizer** - Breaks DSL text into typed tokens
 * 2. **Parser** - Builds a ConditionNode AST from the token stream
 * 3. **Evaluator** - Walks the AST and evaluates conditions against a VedicChart
 * 4. **Runtime** - Manages loading, validation, and batch evaluation of definitions
 *
 * **DSL Grammar (text format):**
 * ```
 * YOGA "Yoga Name" {
 *     CATEGORY: RAJA_YOGA | DHANA_YOGA | SPECIAL_YOGA | CUSTOM
 *     STRENGTH: STRONG | MODERATE | WEAK
 *     CONDITION {
 *         [planet_condition] AND|OR [planet_condition] ...
 *     }
 *     RESULT "Description of the yoga effect"
 * }
 * ```
 *
 * **Supported planet conditions:**
 * - PLANET.SUN IN HOUSE.1
 * - PLANET.JUPITER IN SIGN.CANCER
 * - PLANET.MARS ASPECTS PLANET.SATURN
 * - PLANET.JUPITER ASPECTS HOUSE.7
 * - PLANET.SUN CONJUNCT PLANET.MERCURY
 * - PLANET.MOON IS LORD_OF HOUSE.4
 * - PLANET.JUPITER IS EXALTED / DEBILITATED / RETROGRADE / IN_OWN_SIGN
 * - LORD_OF HOUSE.9 IN HOUSE.10
 * - LORD_OF HOUSE.1 CONJUNCT LORD_OF HOUSE.5
 * - LORD_OF HOUSE.9 ASPECTS LORD_OF HOUSE.10
 * - PLANETS_IN HOUSE.1 COUNT >= 3
 * - HOUSE.7 HAS_BENEFICS
 * - HOUSE.6 HAS_NO_MALEFICS
 * - KENDRA_LORDS IN TRIKONA
 * - TRIKONA_LORDS IN KENDRA
 *
 * **JSON format** is also supported for programmatic definition.
 *
 * Based on classical Vedic astrology principles from:
 * - Brihat Parashara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Saravali
 * - Jataka Parijata
 *
 * @author AstroVajra
 */

// =============================================================================
// DATA MODELS
// =============================================================================

/**
 * Complete runtime holding parsed yoga definitions and any parse errors encountered.
 *
 * @property yogaDefinitions Successfully parsed custom yoga definitions
 * @property parseErrors Errors encountered during parsing (non-fatal; partial parsing continues)
 */
data class AstroDSLRuntime(
    val yogaDefinitions: List<CustomYogaDefinition>,
    val parseErrors: List<ParseError>
)

/**
 * A single custom yoga definition parsed from DSL text or JSON.
 *
 * @property name Human-readable name of the yoga
 * @property category Classification (RAJA_YOGA, DHANA_YOGA, SPECIAL_YOGA, CUSTOM)
 * @property strength Expected strength level (STRONG, MODERATE, WEAK)
 * @property conditions Root node of the condition AST tree
 * @property resultDescription Textual description of the yoga's effects when present
 * @property source Original source text or JSON for debugging
 */
data class CustomYogaDefinition(
    val name: String,
    val category: String,
    val strength: String,
    val conditions: ConditionNode,
    val resultDescription: String,
    val source: String
)

/**
 * Sealed class representing the Abstract Syntax Tree (AST) for yoga conditions.
 *
 * Each node represents a testable astrological condition. Composite nodes (And, Or, Not)
 * combine multiple conditions with boolean logic. Leaf nodes test specific planetary,
 * house, or dignity states.
 */
sealed class ConditionNode {
    /** Logical AND: all child conditions must be true */
    data class And(val conditions: List<ConditionNode>) : ConditionNode()
    /** Logical OR: at least one child condition must be true */
    data class Or(val conditions: List<ConditionNode>) : ConditionNode()
    /** Logical NOT: the child condition must be false */
    data class Not(val condition: ConditionNode) : ConditionNode()

    /**
     * Planet occupies one of the specified houses.
     * @property fromPlanet If non-null, houses are counted from this planet's position (e.g., from Moon)
     */
    data class PlanetInHouse(val planet: Planet, val houses: List<Int>, val fromPlanet: Planet? = null) : ConditionNode()
    /** Planet occupies one of the specified zodiac signs */
    data class PlanetInSign(val planet: Planet, val signs: List<ZodiacSign>) : ConditionNode()
    /** One planet aspects another via Vedic (Parashari) aspects */
    data class PlanetAspectsPlanet(val planet1: Planet, val planet2: Planet) : ConditionNode()
    /** A planet aspects a specified house via Vedic aspects */
    data class PlanetAspectsHouse(val planet: Planet, val house: Int) : ConditionNode()
    /** Two planets are conjunct (same sign) */
    data class PlanetConjunctPlanet(val planet1: Planet, val planet2: Planet) : ConditionNode()
    /** A specific planet is the lord of a specified house */
    data class PlanetIsLordOf(val planet: Planet, val house: Int) : ConditionNode()
    /** Planet is in exaltation */
    data class PlanetIsExalted(val planet: Planet) : ConditionNode()
    /** Planet is in debilitation */
    data class PlanetIsDebilitated(val planet: Planet) : ConditionNode()
    /** Planet is retrograde */
    data class PlanetIsRetrograde(val planet: Planet) : ConditionNode()
    /** Planet is in its own sign (Swakshetra) */
    data class PlanetIsInOwnSign(val planet: Planet) : ConditionNode()
    /** Lord of house N is placed in house M */
    data class HouseLordInHouse(val lordOfHouse: Int, val inHouse: Int) : ConditionNode()
    /** Lords of two houses are conjunct (same sign) */
    data class HouseLordConjunctHouseLord(val house1: Int, val house2: Int) : ConditionNode()
    /** Lord of one house aspects the lord of another house */
    data class HouseLordAspectsHouseLord(val house1: Int, val house2: Int) : ConditionNode()
    /** A house contains at least one natural benefic */
    data class HouseHasBenefics(val house: Int) : ConditionNode()
    /** A house contains no natural malefics */
    data class HouseHasNoMalefics(val house: Int) : ConditionNode()
    /** The number of planets in a house satisfies a comparison (>=, <=, ==, >, <) */
    data class PlanetsInHouseCount(val house: Int, val comparator: String, val count: Int) : ConditionNode()
    /** At least one Kendra lord is placed in a Trikona house */
    data class KendraLordsInTrikona(val dummy: Unit = Unit) : ConditionNode()
    /** At least one Trikona lord is placed in a Kendra house */
    data class TrikonaLordsInKendra(val dummy: Unit = Unit) : ConditionNode()
}

/**
 * Result of evaluating a custom yoga definition against a chart.
 *
 * @property definition The yoga definition that was evaluated
 * @property isPresent Whether all conditions were satisfied
 * @property matchDetails Human-readable descriptions of conditions that matched
 * @property failedConditions Human-readable descriptions of conditions that failed
 */
data class CustomYogaResult(
    val definition: CustomYogaDefinition,
    val isPresent: Boolean,
    val matchDetails: List<String>,
    val failedConditions: List<String>
)

/**
 * Represents a parsing error encountered during DSL compilation.
 *
 * @property line Line number where the error occurred (1-based, 0 if unknown)
 * @property column Column position (0 if unknown)
 * @property message Human-readable error description
 * @property source Fragment of the source text near the error
 */
data class ParseError(
    val line: Int,
    val column: Int,
    val message: String,
    val source: String
)

// =============================================================================
// TOKEN TYPES FOR THE LEXER
// =============================================================================

/**
 * Token types recognized by the AstroDSL lexer.
 */
private enum class TokenType {
    // Keywords
    YOGA, CATEGORY, STRENGTH, CONDITION, RESULT, NOT,
    // Logical operators
    AND, OR,
    // Entity prefixes
    PLANET, HOUSE, SIGN, LORD_OF,
    // Predicates
    IN, ASPECTS, CONJUNCT, IS,
    // Dignity states
    EXALTED, DEBILITATED, RETROGRADE, IN_OWN_SIGN,
    // House group predicates
    HAS_BENEFICS, HAS_NO_MALEFICS,
    PLANETS_IN, COUNT,
    KENDRA_LORDS, TRIKONA_LORDS, KENDRA, TRIKONA,
    // Comparators
    GREATER_EQUAL, LESS_EQUAL, EQUAL, GREATER, LESS,
    // Delimiters
    LBRACE, RBRACE, DOT, COLON, COMMA,
    // Literals
    STRING_LITERAL, NUMBER_LITERAL, IDENTIFIER,
    // End
    EOF
}

/**
 * A single token produced by the lexer.
 */
private data class Token(
    val type: TokenType,
    val value: String,
    val line: Int,
    val column: Int
)

// =============================================================================
// LEXER
// =============================================================================

/**
 * Lexer (tokenizer) for the AstroDSL text format.
 *
 * Converts raw DSL source text into a stream of typed tokens. Handles string literals
 * (double-quoted), numeric literals, keywords, identifiers, operators, and delimiters.
 * Comments starting with `//` are ignored through end-of-line. Block comments using
 * `slash-star ... star-slash` are also supported.
 *
 * @property source The raw DSL source text
 */
private class AstroDSLLexer(private val source: String) {
    private var pos = 0
    private var line = 1
    private var column = 1
    private val tokens = mutableListOf<Token>()

    /** Map of keyword strings to their token types */
    private val keywords = mapOf(
        "YOGA" to TokenType.YOGA,
        "CATEGORY" to TokenType.CATEGORY,
        "STRENGTH" to TokenType.STRENGTH,
        "CONDITION" to TokenType.CONDITION,
        "RESULT" to TokenType.RESULT,
        "NOT" to TokenType.NOT,
        "AND" to TokenType.AND,
        "OR" to TokenType.OR,
        "PLANET" to TokenType.PLANET,
        "HOUSE" to TokenType.HOUSE,
        "SIGN" to TokenType.SIGN,
        "LORD_OF" to TokenType.LORD_OF,
        "IN" to TokenType.IN,
        "ASPECTS" to TokenType.ASPECTS,
        "CONJUNCT" to TokenType.CONJUNCT,
        "IS" to TokenType.IS,
        "EXALTED" to TokenType.EXALTED,
        "DEBILITATED" to TokenType.DEBILITATED,
        "RETROGRADE" to TokenType.RETROGRADE,
        "IN_OWN_SIGN" to TokenType.IN_OWN_SIGN,
        "HAS_BENEFICS" to TokenType.HAS_BENEFICS,
        "HAS_NO_MALEFICS" to TokenType.HAS_NO_MALEFICS,
        "PLANETS_IN" to TokenType.PLANETS_IN,
        "COUNT" to TokenType.COUNT,
        "KENDRA_LORDS" to TokenType.KENDRA_LORDS,
        "TRIKONA_LORDS" to TokenType.TRIKONA_LORDS,
        "KENDRA" to TokenType.KENDRA,
        "TRIKONA" to TokenType.TRIKONA
    )

    /**
     * Tokenize the entire source string.
     * @return List of tokens ending with an EOF token
     */
    fun tokenize(): List<Token> {
        while (pos < source.length) {
            skipWhitespaceAndComments()
            if (pos >= source.length) break

            val ch = source[pos]
            val startLine = line
            val startCol = column

            when {
                ch == '{' -> { tokens.add(Token(TokenType.LBRACE, "{", startLine, startCol)); advance() }
                ch == '}' -> { tokens.add(Token(TokenType.RBRACE, "}", startLine, startCol)); advance() }
                ch == '.' -> { tokens.add(Token(TokenType.DOT, ".", startLine, startCol)); advance() }
                ch == ':' -> { tokens.add(Token(TokenType.COLON, ":", startLine, startCol)); advance() }
                ch == ',' -> { tokens.add(Token(TokenType.COMMA, ",", startLine, startCol)); advance() }
                ch == '"' -> readStringLiteral(startLine, startCol)
                ch == '>' && peek() == '=' -> {
                    tokens.add(Token(TokenType.GREATER_EQUAL, ">=", startLine, startCol))
                    advance(); advance()
                }
                ch == '<' && peek() == '=' -> {
                    tokens.add(Token(TokenType.LESS_EQUAL, "<=", startLine, startCol))
                    advance(); advance()
                }
                ch == '=' && peek() == '=' -> {
                    tokens.add(Token(TokenType.EQUAL, "==", startLine, startCol))
                    advance(); advance()
                }
                ch == '>' -> { tokens.add(Token(TokenType.GREATER, ">", startLine, startCol)); advance() }
                ch == '<' -> { tokens.add(Token(TokenType.LESS, "<", startLine, startCol)); advance() }
                ch.isDigit() -> readNumber(startLine, startCol)
                ch.isLetter() || ch == '_' -> readIdentifierOrKeyword(startLine, startCol)
                else -> advance() // skip unrecognized characters
            }
        }
        tokens.add(Token(TokenType.EOF, "", line, column))
        return tokens
    }

    private fun advance(): Char? {
        if (pos >= source.length) return null
        val ch = source[pos]
        if (ch == '\n') { line++; column = 1 } else { column++ }
        pos++
        return ch
    }

    private fun peek(): Char? = if (pos + 1 < source.length) source[pos + 1] else null

    private fun skipWhitespaceAndComments() {
        while (pos < source.length) {
            val ch = source[pos]
            if (ch.isWhitespace()) {
                advance()
            } else if (ch == '/' && peek() == '/') {
                // Line comment: skip to end of line
                while (pos < source.length && source[pos] != '\n') advance()
            } else if (ch == '/' && peek() == '*') {
                // Block comment: skip to closing */
                advance(); advance() // skip /*
                while (pos < source.length) {
                    if (source[pos] == '*' && peek() == '/') {
                        advance(); advance() // skip */
                        break
                    }
                    advance()
                }
            } else {
                break
            }
        }
    }

    private fun readStringLiteral(startLine: Int, startCol: Int) {
        advance() // skip opening quote
        val sb = StringBuilder()
        while (pos < source.length && source[pos] != '"') {
            if (source[pos] == '\\' && pos + 1 < source.length) {
                advance() // skip backslash
                when (source[pos]) {
                    'n' -> sb.append('\n')
                    't' -> sb.append('\t')
                    '"' -> sb.append('"')
                    '\\' -> sb.append('\\')
                    else -> { sb.append('\\'); sb.append(source[pos]) }
                }
            } else {
                sb.append(source[pos])
            }
            advance()
        }
        if (pos < source.length) advance() // skip closing quote
        tokens.add(Token(TokenType.STRING_LITERAL, sb.toString(), startLine, startCol))
    }

    private fun readNumber(startLine: Int, startCol: Int) {
        val sb = StringBuilder()
        while (pos < source.length && source[pos].isDigit()) {
            sb.append(source[pos])
            advance()
        }
        tokens.add(Token(TokenType.NUMBER_LITERAL, sb.toString(), startLine, startCol))
    }

    private fun readIdentifierOrKeyword(startLine: Int, startCol: Int) {
        val sb = StringBuilder()
        while (pos < source.length && (source[pos].isLetterOrDigit() || source[pos] == '_')) {
            sb.append(source[pos])
            advance()
        }
        val word = sb.toString()
        val tokenType = keywords[word] ?: TokenType.IDENTIFIER
        tokens.add(Token(tokenType, word, startLine, startCol))
    }
}

// =============================================================================
// PARSER
// =============================================================================

/**
 * Recursive descent parser for the AstroDSL text format.
 *
 * Consumes a token stream from the lexer and builds [CustomYogaDefinition] objects
 * with [ConditionNode] ASTs. Recovers from errors by collecting [ParseError] instances
 * and attempting to continue parsing subsequent yoga blocks.
 *
 * @property tokens Token stream from the lexer
 */
private class AstroDSLParser(private val tokens: List<Token>) {
    private var pos = 0
    private val errors = mutableListOf<ParseError>()
    private val definitions = mutableListOf<CustomYogaDefinition>()

    /**
     * Parse all yoga definitions from the token stream.
     * @return Pair of (definitions, errors)
     */
    fun parse(): Pair<List<CustomYogaDefinition>, List<ParseError>> {
        while (!isAtEnd()) {
            try {
                if (check(TokenType.YOGA)) {
                    definitions.add(parseYogaDefinition())
                } else {
                    advance() // skip unexpected tokens at top level
                }
            } catch (e: DSLParseException) {
                errors.add(ParseError(e.line, e.column, e.message ?: "Unknown parse error", e.source))
                // Recovery: skip to next YOGA keyword or end
                while (!isAtEnd() && !check(TokenType.YOGA)) advance()
            }
        }
        return definitions to errors
    }

    private fun parseYogaDefinition(): CustomYogaDefinition {
        val yogaToken = expect(TokenType.YOGA, "Expected YOGA keyword")
        val nameToken = expect(TokenType.STRING_LITERAL, "Expected yoga name as string literal")
        expect(TokenType.LBRACE, "Expected '{' after yoga name")

        var category = "CUSTOM"
        var strength = "MODERATE"
        var conditionNode: ConditionNode? = null
        var resultDescription = ""
        val sourceStart = yogaToken.line

        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            when {
                check(TokenType.CATEGORY) -> {
                    advance()
                    expect(TokenType.COLON, "Expected ':' after CATEGORY")
                    category = expectIdentifier("Expected category value").value
                }
                check(TokenType.STRENGTH) -> {
                    advance()
                    expect(TokenType.COLON, "Expected ':' after STRENGTH")
                    strength = expectIdentifier("Expected strength value").value
                }
                check(TokenType.CONDITION) -> {
                    advance()
                    expect(TokenType.LBRACE, "Expected '{' after CONDITION")
                    conditionNode = parseConditionBlock()
                    expect(TokenType.RBRACE, "Expected '}' to close CONDITION block")
                }
                check(TokenType.RESULT) -> {
                    advance()
                    resultDescription = expect(TokenType.STRING_LITERAL, "Expected result description string").value
                }
                else -> advance() // skip unrecognized tokens inside yoga block
            }
        }
        expect(TokenType.RBRACE, "Expected '}' to close YOGA block")

        if (conditionNode == null) {
            throw DSLParseException(
                sourceStart, 0,
                "Yoga '${nameToken.value}' has no CONDITION block",
                nameToken.value
            )
        }

        return CustomYogaDefinition(
            name = nameToken.value,
            category = category,
            strength = strength,
            conditions = conditionNode,
            resultDescription = resultDescription,
            source = "YOGA \"${nameToken.value}\" { ... }"
        )
    }

    /**
     * Parse the inside of a CONDITION { ... } block.
     * Handles AND/OR chaining between atomic conditions.
     */
    private fun parseConditionBlock(): ConditionNode {
        val conditions = mutableListOf<ConditionNode>()
        var operator: TokenType? = null

        conditions.add(parseAtomicCondition())

        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            if (check(TokenType.AND) || check(TokenType.OR)) {
                val opToken = advance()
                if (operator != null && operator != opToken.type) {
                    // Mixed operators: wrap existing in appropriate node and start fresh group
                    throw DSLParseException(
                        opToken.line, opToken.column,
                        "Cannot mix AND and OR operators at the same level. Use parentheses or nested conditions.",
                        opToken.value
                    )
                }
                operator = opToken.type
                conditions.add(parseAtomicCondition())
            } else {
                break
            }
        }

        return when {
            conditions.size == 1 -> conditions.first()
            operator == TokenType.OR -> ConditionNode.Or(conditions)
            else -> ConditionNode.And(conditions) // default to AND
        }
    }

    /**
     * Parse a single atomic condition expression.
     */
    private fun parseAtomicCondition(): ConditionNode {
        val current = peek()

        return when (current.type) {
            TokenType.NOT -> {
                advance()
                ConditionNode.Not(parseAtomicCondition())
            }
            TokenType.PLANET -> parsePlanetCondition()
            TokenType.LORD_OF -> parseLordOfCondition()
            TokenType.PLANETS_IN -> parsePlanetsInCount()
            TokenType.HOUSE -> parseHouseCondition()
            TokenType.KENDRA_LORDS -> {
                advance()
                expect(TokenType.IN, "Expected IN after KENDRA_LORDS")
                expect(TokenType.TRIKONA, "Expected TRIKONA after IN")
                ConditionNode.KendraLordsInTrikona()
            }
            TokenType.TRIKONA_LORDS -> {
                advance()
                expect(TokenType.IN, "Expected IN after TRIKONA_LORDS")
                expect(TokenType.KENDRA, "Expected KENDRA after IN")
                ConditionNode.TrikonaLordsInKendra()
            }
            else -> throw DSLParseException(
                current.line, current.column,
                "Unexpected token '${current.value}' at start of condition",
                current.value
            )
        }
    }

    /**
     * Parse conditions starting with PLANET.
     * Handles: PLANET.NAME IN HOUSE/SIGN, ASPECTS, CONJUNCT, IS
     */
    private fun parsePlanetCondition(): ConditionNode {
        expect(TokenType.PLANET, "Expected PLANET")
        expect(TokenType.DOT, "Expected '.' after PLANET")
        val planetName = expectIdentifier("Expected planet name").value
        val planet = resolvePlanet(planetName)

        val actionToken = peek()
        return when (actionToken.type) {
            TokenType.IN -> {
                advance()
                parseInTarget(planet)
            }
            TokenType.ASPECTS -> {
                advance()
                parseAspectsTarget(planet)
            }
            TokenType.CONJUNCT -> {
                advance()
                expect(TokenType.PLANET, "Expected PLANET after CONJUNCT")
                expect(TokenType.DOT, "Expected '.' after PLANET")
                val target = resolvePlanet(expectIdentifier("Expected planet name").value)
                ConditionNode.PlanetConjunctPlanet(planet, target)
            }
            TokenType.IS -> {
                advance()
                parseIsCondition(planet)
            }
            else -> throw DSLParseException(
                actionToken.line, actionToken.column,
                "Expected IN, ASPECTS, CONJUNCT, or IS after planet name, got '${actionToken.value}'",
                actionToken.value
            )
        }
    }

    /**
     * Parse "IN HOUSE.N" or "IN SIGN.NAME" after a planet reference.
     */
    private fun parseInTarget(planet: Planet): ConditionNode {
        val targetToken = peek()
        return when (targetToken.type) {
            TokenType.HOUSE -> {
                advance()
                expect(TokenType.DOT, "Expected '.' after HOUSE")
                val houses = parseHouseList()
                // Check for optional FROM clause
                val fromPlanet = if (check(TokenType.IDENTIFIER) && peek().value.equals("FROM", ignoreCase = true)) {
                    advance() // consume FROM
                    expect(TokenType.PLANET, "Expected PLANET after FROM")
                    expect(TokenType.DOT, "Expected '.' after PLANET")
                    resolvePlanet(expectIdentifier("Expected planet name").value)
                } else null
                ConditionNode.PlanetInHouse(planet, houses, fromPlanet)
            }
            TokenType.SIGN -> {
                advance()
                expect(TokenType.DOT, "Expected '.' after SIGN")
                val signs = parseSignList()
                ConditionNode.PlanetInSign(planet, signs)
            }
            else -> throw DSLParseException(
                targetToken.line, targetToken.column,
                "Expected HOUSE or SIGN after IN, got '${targetToken.value}'",
                targetToken.value
            )
        }
    }

    /**
     * Parse "ASPECTS PLANET.NAME" or "ASPECTS HOUSE.N" after a planet reference.
     */
    private fun parseAspectsTarget(planet: Planet): ConditionNode {
        val targetToken = peek()
        return when (targetToken.type) {
            TokenType.PLANET -> {
                advance()
                expect(TokenType.DOT, "Expected '.' after PLANET")
                val target = resolvePlanet(expectIdentifier("Expected planet name").value)
                ConditionNode.PlanetAspectsPlanet(planet, target)
            }
            TokenType.HOUSE -> {
                advance()
                expect(TokenType.DOT, "Expected '.' after HOUSE")
                val house = expect(TokenType.NUMBER_LITERAL, "Expected house number").value.toInt()
                ConditionNode.PlanetAspectsHouse(planet, house)
            }
            else -> throw DSLParseException(
                targetToken.line, targetToken.column,
                "Expected PLANET or HOUSE after ASPECTS, got '${targetToken.value}'",
                targetToken.value
            )
        }
    }

    /**
     * Parse "IS EXALTED / DEBILITATED / RETROGRADE / IN_OWN_SIGN / LORD_OF HOUSE.N"
     */
    private fun parseIsCondition(planet: Planet): ConditionNode {
        val stateToken = peek()
        return when (stateToken.type) {
            TokenType.EXALTED -> { advance(); ConditionNode.PlanetIsExalted(planet) }
            TokenType.DEBILITATED -> { advance(); ConditionNode.PlanetIsDebilitated(planet) }
            TokenType.RETROGRADE -> { advance(); ConditionNode.PlanetIsRetrograde(planet) }
            TokenType.IN_OWN_SIGN -> { advance(); ConditionNode.PlanetIsInOwnSign(planet) }
            TokenType.LORD_OF -> {
                advance()
                expect(TokenType.HOUSE, "Expected HOUSE after LORD_OF")
                expect(TokenType.DOT, "Expected '.' after HOUSE")
                val house = expect(TokenType.NUMBER_LITERAL, "Expected house number").value.toInt()
                ConditionNode.PlanetIsLordOf(planet, house)
            }
            else -> throw DSLParseException(
                stateToken.line, stateToken.column,
                "Expected EXALTED, DEBILITATED, RETROGRADE, IN_OWN_SIGN, or LORD_OF after IS, got '${stateToken.value}'",
                stateToken.value
            )
        }
    }

    /**
     * Parse conditions starting with LORD_OF.
     * Handles: LORD_OF HOUSE.N IN HOUSE.M, LORD_OF HOUSE.N CONJUNCT LORD_OF HOUSE.M,
     * LORD_OF HOUSE.N ASPECTS LORD_OF HOUSE.M
     */
    private fun parseLordOfCondition(): ConditionNode {
        expect(TokenType.LORD_OF, "Expected LORD_OF")
        expect(TokenType.HOUSE, "Expected HOUSE after LORD_OF")
        expect(TokenType.DOT, "Expected '.' after HOUSE")
        val house1 = expect(TokenType.NUMBER_LITERAL, "Expected house number").value.toInt()

        val actionToken = peek()
        return when (actionToken.type) {
            TokenType.IN -> {
                advance()
                expect(TokenType.HOUSE, "Expected HOUSE after IN")
                expect(TokenType.DOT, "Expected '.' after HOUSE")
                val house2 = expect(TokenType.NUMBER_LITERAL, "Expected house number").value.toInt()
                ConditionNode.HouseLordInHouse(house1, house2)
            }
            TokenType.CONJUNCT -> {
                advance()
                expect(TokenType.LORD_OF, "Expected LORD_OF after CONJUNCT")
                expect(TokenType.HOUSE, "Expected HOUSE after LORD_OF")
                expect(TokenType.DOT, "Expected '.' after HOUSE")
                val house2 = expect(TokenType.NUMBER_LITERAL, "Expected house number").value.toInt()
                ConditionNode.HouseLordConjunctHouseLord(house1, house2)
            }
            TokenType.ASPECTS -> {
                advance()
                expect(TokenType.LORD_OF, "Expected LORD_OF after ASPECTS")
                expect(TokenType.HOUSE, "Expected HOUSE after LORD_OF")
                expect(TokenType.DOT, "Expected '.' after HOUSE")
                val house2 = expect(TokenType.NUMBER_LITERAL, "Expected house number").value.toInt()
                ConditionNode.HouseLordAspectsHouseLord(house1, house2)
            }
            else -> throw DSLParseException(
                actionToken.line, actionToken.column,
                "Expected IN, CONJUNCT, or ASPECTS after LORD_OF HOUSE.N, got '${actionToken.value}'",
                actionToken.value
            )
        }
    }

    /**
     * Parse PLANETS_IN HOUSE.N COUNT >= M
     */
    private fun parsePlanetsInCount(): ConditionNode {
        expect(TokenType.PLANETS_IN, "Expected PLANETS_IN")
        expect(TokenType.HOUSE, "Expected HOUSE after PLANETS_IN")
        expect(TokenType.DOT, "Expected '.' after HOUSE")
        val house = expect(TokenType.NUMBER_LITERAL, "Expected house number").value.toInt()
        expect(TokenType.COUNT, "Expected COUNT after house number")
        val comparator = parseComparator()
        val count = expect(TokenType.NUMBER_LITERAL, "Expected count value").value.toInt()
        return ConditionNode.PlanetsInHouseCount(house, comparator, count)
    }

    /**
     * Parse HOUSE.N HAS_BENEFICS or HOUSE.N HAS_NO_MALEFICS
     */
    private fun parseHouseCondition(): ConditionNode {
        expect(TokenType.HOUSE, "Expected HOUSE")
        expect(TokenType.DOT, "Expected '.' after HOUSE")
        val house = expect(TokenType.NUMBER_LITERAL, "Expected house number").value.toInt()

        val predicateToken = peek()
        return when (predicateToken.type) {
            TokenType.HAS_BENEFICS -> { advance(); ConditionNode.HouseHasBenefics(house) }
            TokenType.HAS_NO_MALEFICS -> { advance(); ConditionNode.HouseHasNoMalefics(house) }
            else -> throw DSLParseException(
                predicateToken.line, predicateToken.column,
                "Expected HAS_BENEFICS or HAS_NO_MALEFICS after HOUSE.N, got '${predicateToken.value}'",
                predicateToken.value
            )
        }
    }

    private fun parseComparator(): String {
        val token = peek()
        return when (token.type) {
            TokenType.GREATER_EQUAL -> { advance(); ">=" }
            TokenType.LESS_EQUAL -> { advance(); "<=" }
            TokenType.EQUAL -> { advance(); "==" }
            TokenType.GREATER -> { advance(); ">" }
            TokenType.LESS -> { advance(); "<" }
            else -> throw DSLParseException(
                token.line, token.column,
                "Expected comparator (>=, <=, ==, >, <), got '${token.value}'",
                token.value
            )
        }
    }

    /**
     * Parse a house number or comma-separated list of house numbers.
     * Example: 1 or 1,4,7,10
     */
    private fun parseHouseList(): List<Int> {
        val houses = mutableListOf<Int>()
        houses.add(expect(TokenType.NUMBER_LITERAL, "Expected house number").value.toInt())
        while (check(TokenType.COMMA)) {
            advance()
            houses.add(expect(TokenType.NUMBER_LITERAL, "Expected house number after comma").value.toInt())
        }
        return houses
    }

    /**
     * Parse a sign name or comma-separated list of sign names.
     */
    private fun parseSignList(): List<ZodiacSign> {
        val signs = mutableListOf<ZodiacSign>()
        signs.add(resolveSign(expectIdentifier("Expected sign name").value))
        while (check(TokenType.COMMA)) {
            advance()
            signs.add(resolveSign(expectIdentifier("Expected sign name after comma").value))
        }
        return signs
    }

    // --- Token navigation helpers ---

    private fun peek(): Token = tokens[pos.coerceAtMost(tokens.lastIndex)]

    private fun check(type: TokenType): Boolean = peek().type == type

    private fun isAtEnd(): Boolean = peek().type == TokenType.EOF

    private fun advance(): Token {
        val current = peek()
        if (!isAtEnd()) pos++
        return current
    }

    private fun expect(type: TokenType, errorMessage: String): Token {
        val current = peek()
        if (current.type != type) {
            throw DSLParseException(current.line, current.column, errorMessage, current.value)
        }
        return advance()
    }

    private fun expectIdentifier(errorMessage: String): Token {
        val current = peek()
        if (current.type != TokenType.IDENTIFIER && current.type != TokenType.NUMBER_LITERAL) {
            // Also accept keywords that double as identifiers in context
            if (current.type in setOf(
                    TokenType.KENDRA, TokenType.TRIKONA,
                    TokenType.EXALTED, TokenType.DEBILITATED,
                    TokenType.RETROGRADE, TokenType.IN_OWN_SIGN
                )) {
                return advance()
            }
            throw DSLParseException(current.line, current.column, errorMessage, current.value)
        }
        return advance()
    }

    // --- Resolution helpers ---

    private fun resolvePlanet(name: String): Planet {
        return when (name.uppercase()) {
            "SUN" -> Planet.SUN
            "MOON" -> Planet.MOON
            "MARS" -> Planet.MARS
            "MERCURY" -> Planet.MERCURY
            "JUPITER" -> Planet.JUPITER
            "VENUS" -> Planet.VENUS
            "SATURN" -> Planet.SATURN
            "RAHU" -> Planet.RAHU
            "KETU" -> Planet.KETU
            "URANUS" -> Planet.URANUS
            "NEPTUNE" -> Planet.NEPTUNE
            "PLUTO" -> Planet.PLUTO
            else -> throw DSLParseException(
                peek().line, peek().column,
                "Unknown planet: $name",
                name
            )
        }
    }

    private fun resolveSign(name: String): ZodiacSign {
        return when (name.uppercase()) {
            "ARIES" -> ZodiacSign.ARIES
            "TAURUS" -> ZodiacSign.TAURUS
            "GEMINI" -> ZodiacSign.GEMINI
            "CANCER" -> ZodiacSign.CANCER
            "LEO" -> ZodiacSign.LEO
            "VIRGO" -> ZodiacSign.VIRGO
            "LIBRA" -> ZodiacSign.LIBRA
            "SCORPIO" -> ZodiacSign.SCORPIO
            "SAGITTARIUS" -> ZodiacSign.SAGITTARIUS
            "CAPRICORN" -> ZodiacSign.CAPRICORN
            "AQUARIUS" -> ZodiacSign.AQUARIUS
            "PISCES" -> ZodiacSign.PISCES
            else -> throw DSLParseException(
                peek().line, peek().column,
                "Unknown zodiac sign: $name",
                name
            )
        }
    }
}

/**
 * Internal exception for parse errors with position information.
 */
private class DSLParseException(
    val line: Int,
    val column: Int,
    override val message: String,
    val source: String
) : Exception(message)

// =============================================================================
// JSON PARSER
// =============================================================================

/**
 * Parser for JSON-format custom yoga definitions.
 *
 * Accepts JSON objects or arrays of objects conforming to the AstroDSL JSON schema:
 * ```json
 * {
 *   "name": "Yoga Name",
 *   "category": "RAJA_YOGA",
 *   "strength": "STRONG",
 *   "conditions": [ ... ],
 *   "logic": "AND",
 *   "result": "Effect description"
 * }
 * ```
 *
 * Condition types supported:
 * - planet_in_house, planet_in_sign, planet_aspects_planet, planet_aspects_house
 * - planet_conjunct_planet, planet_is_lord_of, planet_exalted, planet_debilitated
 * - planet_retrograde, planet_in_own_sign, planet_not_debilitated, planet_not_combust
 * - lord_in_house, lord_conjunct_lord, lord_aspects_lord
 * - house_has_benefics, house_has_no_malefics, planets_in_house_count
 * - kendra_lords_in_trikona, trikona_lords_in_kendra
 */
private object AstroDSLJsonParser {

    /**
     * Parse a JSON string (object or array) into yoga definitions.
     * @return Pair of (definitions, errors)
     */
    fun parse(json: String): Pair<List<CustomYogaDefinition>, List<ParseError>> {
        val definitions = mutableListOf<CustomYogaDefinition>()
        val errors = mutableListOf<ParseError>()

        try {
            val trimmed = json.trim()
            if (trimmed.startsWith("[")) {
                val array = JSONArray(trimmed)
                for (i in 0 until array.length()) {
                    try {
                        definitions.add(parseYogaObject(array.getJSONObject(i)))
                    } catch (e: Exception) {
                        errors.add(ParseError(0, 0, "Error parsing yoga at index $i: ${e.message}", json.take(100)))
                    }
                }
            } else if (trimmed.startsWith("{")) {
                try {
                    definitions.add(parseYogaObject(JSONObject(trimmed)))
                } catch (e: Exception) {
                    errors.add(ParseError(0, 0, "Error parsing yoga JSON: ${e.message}", json.take(100)))
                }
            } else {
                errors.add(ParseError(0, 0, "JSON must start with '{' or '['", json.take(50)))
            }
        } catch (e: Exception) {
            errors.add(ParseError(0, 0, "Invalid JSON: ${e.message}", json.take(100)))
        }

        return definitions to errors
    }

    private fun parseYogaObject(obj: JSONObject): CustomYogaDefinition {
        val name = obj.getString("name")
        val category = obj.optString("category", "CUSTOM")
        val strength = obj.optString("strength", "MODERATE")
        val result = obj.optString("result", "")
        val logic = obj.optString("logic", "AND")

        val conditionsArray = obj.getJSONArray("conditions")
        val conditionNodes = mutableListOf<ConditionNode>()

        for (i in 0 until conditionsArray.length()) {
            conditionNodes.add(parseConditionObject(conditionsArray.getJSONObject(i)))
        }

        val rootCondition = when {
            conditionNodes.size == 1 -> conditionNodes.first()
            logic.uppercase() == "OR" -> ConditionNode.Or(conditionNodes)
            else -> ConditionNode.And(conditionNodes)
        }

        return CustomYogaDefinition(
            name = name,
            category = category,
            strength = strength,
            conditions = rootCondition,
            resultDescription = result,
            source = obj.toString()
        )
    }

    private fun parseConditionObject(obj: JSONObject): ConditionNode {
        val type = obj.getString("type")

        return when (type) {
            "planet_in_house" -> {
                val planet = resolvePlanet(obj.getString("planet"))
                val houses = parseIntList(obj, "houses")
                val fromPlanet = if (obj.has("from")) resolvePlanet(obj.getString("from")) else null
                ConditionNode.PlanetInHouse(planet, houses, fromPlanet)
            }
            "planet_in_sign" -> {
                val planet = resolvePlanet(obj.getString("planet"))
                val signs = parseSignList(obj, "signs")
                ConditionNode.PlanetInSign(planet, signs)
            }
            "planet_aspects_planet" -> {
                val p1 = resolvePlanet(obj.getString("planet"))
                val p2 = resolvePlanet(obj.getString("target"))
                ConditionNode.PlanetAspectsPlanet(p1, p2)
            }
            "planet_aspects_house" -> {
                val planet = resolvePlanet(obj.getString("planet"))
                val house = obj.getInt("house")
                ConditionNode.PlanetAspectsHouse(planet, house)
            }
            "planet_conjunct_planet" -> {
                val p1 = resolvePlanet(obj.getString("planet"))
                val p2 = resolvePlanet(obj.getString("target"))
                ConditionNode.PlanetConjunctPlanet(p1, p2)
            }
            "planet_is_lord_of" -> {
                val planet = resolvePlanet(obj.getString("planet"))
                val house = obj.getInt("house")
                ConditionNode.PlanetIsLordOf(planet, house)
            }
            "planet_exalted" -> {
                val planet = resolvePlanet(obj.getString("planet"))
                ConditionNode.PlanetIsExalted(planet)
            }
            "planet_debilitated" -> {
                val planet = resolvePlanet(obj.getString("planet"))
                ConditionNode.PlanetIsDebilitated(planet)
            }
            "planet_not_debilitated" -> {
                val planet = resolvePlanet(obj.getString("planet"))
                ConditionNode.Not(ConditionNode.PlanetIsDebilitated(planet))
            }
            "planet_not_combust" -> {
                // Combustion check: planet within combustion orb of Sun - modeled as Not(conjunct Sun within orb)
                // For DSL simplicity, we treat "not combust" as "not conjunct Sun" as a proxy
                val planet = resolvePlanet(obj.getString("planet"))
                ConditionNode.Not(ConditionNode.PlanetConjunctPlanet(planet, Planet.SUN))
            }
            "planet_retrograde" -> {
                val planet = resolvePlanet(obj.getString("planet"))
                ConditionNode.PlanetIsRetrograde(planet)
            }
            "planet_in_own_sign" -> {
                val planet = resolvePlanet(obj.getString("planet"))
                ConditionNode.PlanetIsInOwnSign(planet)
            }
            "lord_in_house" -> {
                val lordOfHouse = obj.getInt("lord_of_house")
                val inHouse = obj.getInt("in_house")
                ConditionNode.HouseLordInHouse(lordOfHouse, inHouse)
            }
            "lord_conjunct_lord" -> {
                val h1 = obj.getInt("house1")
                val h2 = obj.getInt("house2")
                ConditionNode.HouseLordConjunctHouseLord(h1, h2)
            }
            "lord_aspects_lord" -> {
                val h1 = obj.getInt("house1")
                val h2 = obj.getInt("house2")
                ConditionNode.HouseLordAspectsHouseLord(h1, h2)
            }
            "house_has_benefics" -> {
                val house = obj.getInt("house")
                ConditionNode.HouseHasBenefics(house)
            }
            "house_has_no_malefics" -> {
                val house = obj.getInt("house")
                ConditionNode.HouseHasNoMalefics(house)
            }
            "planets_in_house_count" -> {
                val house = obj.getInt("house")
                val comparator = obj.optString("comparator", ">=")
                val count = obj.getInt("count")
                ConditionNode.PlanetsInHouseCount(house, comparator, count)
            }
            "kendra_lords_in_trikona" -> ConditionNode.KendraLordsInTrikona()
            "trikona_lords_in_kendra" -> ConditionNode.TrikonaLordsInKendra()
            "and" -> {
                val subConditions = obj.getJSONArray("conditions")
                val nodes = (0 until subConditions.length()).map { parseConditionObject(subConditions.getJSONObject(it)) }
                ConditionNode.And(nodes)
            }
            "or" -> {
                val subConditions = obj.getJSONArray("conditions")
                val nodes = (0 until subConditions.length()).map { parseConditionObject(subConditions.getJSONObject(it)) }
                ConditionNode.Or(nodes)
            }
            "not" -> {
                val inner = parseConditionObject(obj.getJSONObject("condition"))
                ConditionNode.Not(inner)
            }
            else -> throw IllegalArgumentException("Unknown condition type: $type")
        }
    }

    private fun parseIntList(obj: JSONObject, key: String): List<Int> {
        val array = obj.getJSONArray(key)
        return (0 until array.length()).map { array.getInt(it) }
    }

    private fun parseSignList(obj: JSONObject, key: String): List<ZodiacSign> {
        val array = obj.getJSONArray(key)
        return (0 until array.length()).map { resolveSign(array.getString(it)) }
    }

    private fun resolvePlanet(name: String): Planet {
        return when (name.uppercase()) {
            "SUN" -> Planet.SUN
            "MOON" -> Planet.MOON
            "MARS" -> Planet.MARS
            "MERCURY" -> Planet.MERCURY
            "JUPITER" -> Planet.JUPITER
            "VENUS" -> Planet.VENUS
            "SATURN" -> Planet.SATURN
            "RAHU" -> Planet.RAHU
            "KETU" -> Planet.KETU
            "URANUS" -> Planet.URANUS
            "NEPTUNE" -> Planet.NEPTUNE
            "PLUTO" -> Planet.PLUTO
            else -> throw IllegalArgumentException("Unknown planet: $name")
        }
    }

    private fun resolveSign(name: String): ZodiacSign {
        return when (name.uppercase()) {
            "ARIES" -> ZodiacSign.ARIES
            "TAURUS" -> ZodiacSign.TAURUS
            "GEMINI" -> ZodiacSign.GEMINI
            "CANCER" -> ZodiacSign.CANCER
            "LEO" -> ZodiacSign.LEO
            "VIRGO" -> ZodiacSign.VIRGO
            "LIBRA" -> ZodiacSign.LIBRA
            "SCORPIO" -> ZodiacSign.SCORPIO
            "SAGITTARIUS" -> ZodiacSign.SAGITTARIUS
            "CAPRICORN" -> ZodiacSign.CAPRICORN
            "AQUARIUS" -> ZodiacSign.AQUARIUS
            "PISCES" -> ZodiacSign.PISCES
            else -> throw IllegalArgumentException("Unknown zodiac sign: $name")
        }
    }
}

// =============================================================================
// CONDITION EVALUATOR
// =============================================================================

/**
 * Evaluates [ConditionNode] AST trees against a [VedicChart].
 *
 * Uses Vedic (Parashari) astrology rules for:
 * - **Aspects**: Standard 7th aspect + Mars (4,8), Jupiter (5,9), Saturn (3,10)
 * - **Conjunction**: Planets in the same sign (same house via whole sign reckoning)
 * - **House lordship**: Sign ruler of the house cusp
 * - **Benefics**: Jupiter, Venus, Mercury (when unafflicted), waxing Moon
 * - **Malefics**: Sun, Mars, Saturn, Rahu, Ketu
 * - **Dignity**: Exaltation, debilitation, own sign via [VedicAstrologyUtils]
 *
 * @property chart The VedicChart to evaluate conditions against
 */
private class ConditionEvaluator(private val chart: VedicChart) {

    /** Cached map of planet positions for O(1) lookup */
    private val planetMap: Map<Planet, PlanetPosition> by lazy {
        chart.planetPositions.associateBy { it.planet }
    }

    /** Cached house lords: house (1-12) -> Planet */
    private val houseLords: Map<Int, Planet> by lazy {
        (1..12).associateWith { house -> VedicAstrologyUtils.getHouseLord(chart, house) }
    }

    /** Cached planets grouped by house */
    private val planetsByHouse: Map<Int, List<PlanetPosition>> by lazy {
        chart.planetPositions.groupBy { it.house }
    }

    /**
     * Evaluate a condition node and return (result, matchDetails, failedConditions).
     */
    fun evaluate(node: ConditionNode): Triple<Boolean, List<String>, List<String>> {
        return when (node) {
            is ConditionNode.And -> evaluateAnd(node)
            is ConditionNode.Or -> evaluateOr(node)
            is ConditionNode.Not -> evaluateNot(node)
            is ConditionNode.PlanetInHouse -> evaluatePlanetInHouse(node)
            is ConditionNode.PlanetInSign -> evaluatePlanetInSign(node)
            is ConditionNode.PlanetAspectsPlanet -> evaluatePlanetAspectsPlanet(node)
            is ConditionNode.PlanetAspectsHouse -> evaluatePlanetAspectsHouse(node)
            is ConditionNode.PlanetConjunctPlanet -> evaluatePlanetConjunctPlanet(node)
            is ConditionNode.PlanetIsLordOf -> evaluatePlanetIsLordOf(node)
            is ConditionNode.PlanetIsExalted -> evaluatePlanetIsExalted(node)
            is ConditionNode.PlanetIsDebilitated -> evaluatePlanetIsDebilitated(node)
            is ConditionNode.PlanetIsRetrograde -> evaluatePlanetIsRetrograde(node)
            is ConditionNode.PlanetIsInOwnSign -> evaluatePlanetIsInOwnSign(node)
            is ConditionNode.HouseLordInHouse -> evaluateHouseLordInHouse(node)
            is ConditionNode.HouseLordConjunctHouseLord -> evaluateHouseLordConjunctHouseLord(node)
            is ConditionNode.HouseLordAspectsHouseLord -> evaluateHouseLordAspectsHouseLord(node)
            is ConditionNode.HouseHasBenefics -> evaluateHouseHasBenefics(node)
            is ConditionNode.HouseHasNoMalefics -> evaluateHouseHasNoMalefics(node)
            is ConditionNode.PlanetsInHouseCount -> evaluatePlanetsInHouseCount(node)
            is ConditionNode.KendraLordsInTrikona -> evaluateKendraLordsInTrikona(node)
            is ConditionNode.TrikonaLordsInKendra -> evaluateTrikonaLordsInKendra(node)
        }
    }

    // --- Composite evaluators ---

    private fun evaluateAnd(node: ConditionNode.And): Triple<Boolean, List<String>, List<String>> {
        val allMatches = mutableListOf<String>()
        val allFailed = mutableListOf<String>()
        var allTrue = true
        for (child in node.conditions) {
            val (result, matches, failed) = evaluate(child)
            allMatches.addAll(matches)
            allFailed.addAll(failed)
            if (!result) allTrue = false
        }
        return Triple(allTrue, allMatches, allFailed)
    }

    private fun evaluateOr(node: ConditionNode.Or): Triple<Boolean, List<String>, List<String>> {
        val allMatches = mutableListOf<String>()
        val allFailed = mutableListOf<String>()
        var anyTrue = false
        for (child in node.conditions) {
            val (result, matches, failed) = evaluate(child)
            allMatches.addAll(matches)
            allFailed.addAll(failed)
            if (result) anyTrue = true
        }
        return Triple(anyTrue, allMatches, allFailed)
    }

    private fun evaluateNot(node: ConditionNode.Not): Triple<Boolean, List<String>, List<String>> {
        val (result, matches, failed) = evaluate(node.condition)
        val notResult = !result
        return if (notResult) {
            Triple(true, listOf("NOT condition satisfied"), failed)
        } else {
            Triple(false, matches, listOf("NOT condition failed (inner condition was true)"))
        }
    }

    // --- Atomic evaluators ---

    private fun evaluatePlanetInHouse(node: ConditionNode.PlanetInHouse): Triple<Boolean, List<String>, List<String>> {
        val pos = planetMap[node.planet]
            ?: return Triple(false, emptyList(), listOf("${node.planet.displayName} not found in chart"))

        val effectiveHouse = if (node.fromPlanet != null) {
            val fromPos = planetMap[node.fromPlanet]
                ?: return Triple(false, emptyList(), listOf("${node.fromPlanet.displayName} not found in chart"))
            // Calculate house of planet from fromPlanet's position
            val diff = ((pos.house - fromPos.house + 12) % 12) + 1
            diff
        } else {
            pos.house
        }

        val result = effectiveHouse in node.houses
        val desc = if (node.fromPlanet != null) {
            "${node.planet.displayName} in house $effectiveHouse from ${node.fromPlanet.displayName} (target houses: ${node.houses})"
        } else {
            "${node.planet.displayName} in house ${pos.house} (target houses: ${node.houses})"
        }
        return if (result) {
            Triple(true, listOf(desc), emptyList())
        } else {
            Triple(false, emptyList(), listOf(desc))
        }
    }

    private fun evaluatePlanetInSign(node: ConditionNode.PlanetInSign): Triple<Boolean, List<String>, List<String>> {
        val pos = planetMap[node.planet]
            ?: return Triple(false, emptyList(), listOf("${node.planet.displayName} not found in chart"))
        val result = pos.sign in node.signs
        val desc = "${node.planet.displayName} in ${pos.sign.displayName} (target signs: ${node.signs.map { it.displayName }})"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluatePlanetAspectsPlanet(node: ConditionNode.PlanetAspectsPlanet): Triple<Boolean, List<String>, List<String>> {
        val pos1 = planetMap[node.planet1]
            ?: return Triple(false, emptyList(), listOf("${node.planet1.displayName} not found in chart"))
        val pos2 = planetMap[node.planet2]
            ?: return Triple(false, emptyList(), listOf("${node.planet2.displayName} not found in chart"))

        val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(node.planet1, pos1.house)
        val result = pos2.house in aspectedHouses
        val desc = "${node.planet1.displayName} (house ${pos1.house}) aspects ${node.planet2.displayName} (house ${pos2.house})"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluatePlanetAspectsHouse(node: ConditionNode.PlanetAspectsHouse): Triple<Boolean, List<String>, List<String>> {
        val pos = planetMap[node.planet]
            ?: return Triple(false, emptyList(), listOf("${node.planet.displayName} not found in chart"))

        val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(node.planet, pos.house)
        val result = node.house in aspectedHouses
        val desc = "${node.planet.displayName} (house ${pos.house}) aspects house ${node.house}"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluatePlanetConjunctPlanet(node: ConditionNode.PlanetConjunctPlanet): Triple<Boolean, List<String>, List<String>> {
        val pos1 = planetMap[node.planet1]
            ?: return Triple(false, emptyList(), listOf("${node.planet1.displayName} not found in chart"))
        val pos2 = planetMap[node.planet2]
            ?: return Triple(false, emptyList(), listOf("${node.planet2.displayName} not found in chart"))

        // Conjunction in Vedic astrology = same sign (same rashi)
        val result = pos1.sign == pos2.sign
        val desc = "${node.planet1.displayName} (${pos1.sign.displayName}) conjunct ${node.planet2.displayName} (${pos2.sign.displayName})"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluatePlanetIsLordOf(node: ConditionNode.PlanetIsLordOf): Triple<Boolean, List<String>, List<String>> {
        val validHouse = node.house.coerceIn(1, 12)
        val lord = houseLords[validHouse]
        val result = lord == node.planet
        val desc = "${node.planet.displayName} is lord of house $validHouse (actual lord: ${lord?.displayName ?: "unknown"})"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluatePlanetIsExalted(node: ConditionNode.PlanetIsExalted): Triple<Boolean, List<String>, List<String>> {
        val pos = planetMap[node.planet]
            ?: return Triple(false, emptyList(), listOf("${node.planet.displayName} not found in chart"))
        val result = VedicAstrologyUtils.isExalted(pos)
        val desc = "${node.planet.displayName} exalted in ${pos.sign.displayName}"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf("${node.planet.displayName} NOT exalted (in ${pos.sign.displayName})"))
    }

    private fun evaluatePlanetIsDebilitated(node: ConditionNode.PlanetIsDebilitated): Triple<Boolean, List<String>, List<String>> {
        val pos = planetMap[node.planet]
            ?: return Triple(false, emptyList(), listOf("${node.planet.displayName} not found in chart"))
        val result = VedicAstrologyUtils.isDebilitated(pos)
        val desc = "${node.planet.displayName} debilitated in ${pos.sign.displayName}"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf("${node.planet.displayName} NOT debilitated (in ${pos.sign.displayName})"))
    }

    private fun evaluatePlanetIsRetrograde(node: ConditionNode.PlanetIsRetrograde): Triple<Boolean, List<String>, List<String>> {
        val pos = planetMap[node.planet]
            ?: return Triple(false, emptyList(), listOf("${node.planet.displayName} not found in chart"))
        val result = pos.isRetrograde
        val desc = "${node.planet.displayName} is ${if (result) "" else "NOT "}retrograde"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluatePlanetIsInOwnSign(node: ConditionNode.PlanetIsInOwnSign): Triple<Boolean, List<String>, List<String>> {
        val pos = planetMap[node.planet]
            ?: return Triple(false, emptyList(), listOf("${node.planet.displayName} not found in chart"))
        val result = VedicAstrologyUtils.isInOwnSign(pos)
        val desc = "${node.planet.displayName} in own sign ${pos.sign.displayName}"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf("${node.planet.displayName} NOT in own sign (in ${pos.sign.displayName})"))
    }

    private fun evaluateHouseLordInHouse(node: ConditionNode.HouseLordInHouse): Triple<Boolean, List<String>, List<String>> {
        val lord = houseLords[node.lordOfHouse.coerceIn(1, 12)]
            ?: return Triple(false, emptyList(), listOf("Cannot determine lord of house ${node.lordOfHouse}"))
        val lordPos = planetMap[lord]
            ?: return Triple(false, emptyList(), listOf("${lord.displayName} (lord of house ${node.lordOfHouse}) not found"))
        val result = lordPos.house == node.inHouse
        val desc = "Lord of house ${node.lordOfHouse} (${lord.displayName}) in house ${lordPos.house} (target: ${node.inHouse})"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluateHouseLordConjunctHouseLord(node: ConditionNode.HouseLordConjunctHouseLord): Triple<Boolean, List<String>, List<String>> {
        val lord1 = houseLords[node.house1.coerceIn(1, 12)]
        val lord2 = houseLords[node.house2.coerceIn(1, 12)]
        if (lord1 == null || lord2 == null) {
            return Triple(false, emptyList(), listOf("Cannot determine lords of houses ${node.house1} and ${node.house2}"))
        }
        // Same planet lords both houses
        if (lord1 == lord2) {
            val desc = "Houses ${node.house1} and ${node.house2} share lord ${lord1.displayName} (auto-conjunct)"
            return Triple(true, listOf(desc), emptyList())
        }
        val pos1 = planetMap[lord1]
        val pos2 = planetMap[lord2]
        if (pos1 == null || pos2 == null) {
            return Triple(false, emptyList(), listOf("Lord positions not found"))
        }
        val result = pos1.sign == pos2.sign
        val desc = "Lord of ${node.house1} (${lord1.displayName}, ${pos1.sign.displayName}) conjunct lord of ${node.house2} (${lord2.displayName}, ${pos2.sign.displayName})"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluateHouseLordAspectsHouseLord(node: ConditionNode.HouseLordAspectsHouseLord): Triple<Boolean, List<String>, List<String>> {
        val lord1 = houseLords[node.house1.coerceIn(1, 12)]
        val lord2 = houseLords[node.house2.coerceIn(1, 12)]
        if (lord1 == null || lord2 == null) {
            return Triple(false, emptyList(), listOf("Cannot determine lords of houses ${node.house1} and ${node.house2}"))
        }
        val pos1 = planetMap[lord1]
        val pos2 = planetMap[lord2]
        if (pos1 == null || pos2 == null) {
            return Triple(false, emptyList(), listOf("Lord positions not found"))
        }
        val aspectedHouses = VedicAstrologyUtils.getAspectedHouses(lord1, pos1.house)
        val result = pos2.house in aspectedHouses
        val desc = "Lord of ${node.house1} (${lord1.displayName}, house ${pos1.house}) aspects lord of ${node.house2} (${lord2.displayName}, house ${pos2.house})"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluateHouseHasBenefics(node: ConditionNode.HouseHasBenefics): Triple<Boolean, List<String>, List<String>> {
        val planetsInHouse = planetsByHouse[node.house] ?: emptyList()
        val beneficsPresent = planetsInHouse.filter { VedicAstrologyUtils.isNaturalBenefic(it.planet) }
        val result = beneficsPresent.isNotEmpty()
        val desc = "House ${node.house} has benefics: ${beneficsPresent.map { it.planet.displayName }}"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf("House ${node.house} has no benefics"))
    }

    private fun evaluateHouseHasNoMalefics(node: ConditionNode.HouseHasNoMalefics): Triple<Boolean, List<String>, List<String>> {
        val planetsInHouse = planetsByHouse[node.house] ?: emptyList()
        val maleficsPresent = planetsInHouse.filter { VedicAstrologyUtils.isNaturalMalefic(it.planet) }
        val result = maleficsPresent.isEmpty()
        return if (result) {
            Triple(true, listOf("House ${node.house} has no malefics"), emptyList())
        } else {
            Triple(false, emptyList(), listOf("House ${node.house} has malefics: ${maleficsPresent.map { it.planet.displayName }}"))
        }
    }

    private fun evaluatePlanetsInHouseCount(node: ConditionNode.PlanetsInHouseCount): Triple<Boolean, List<String>, List<String>> {
        val planetsInHouse = planetsByHouse[node.house] ?: emptyList()
        val actualCount = planetsInHouse.size
        val result = when (node.comparator) {
            ">=" -> actualCount >= node.count
            "<=" -> actualCount <= node.count
            "==" -> actualCount == node.count
            ">" -> actualCount > node.count
            "<" -> actualCount < node.count
            else -> false
        }
        val desc = "House ${node.house} has $actualCount planets (condition: ${node.comparator} ${node.count})"
        return if (result) Triple(true, listOf(desc), emptyList())
        else Triple(false, emptyList(), listOf(desc))
    }

    private fun evaluateKendraLordsInTrikona(node: ConditionNode.KendraLordsInTrikona): Triple<Boolean, List<String>, List<String>> {
        val kendraHouses = listOf(1, 4, 7, 10)
        val trikonaHouses = listOf(1, 5, 9)
        val matchDetails = mutableListOf<String>()

        for (kh in kendraHouses) {
            val lord = houseLords[kh] ?: continue
            val lordPos = planetMap[lord] ?: continue
            if (lordPos.house in trikonaHouses) {
                matchDetails.add("Lord of Kendra $kh (${lord.displayName}) is in Trikona house ${lordPos.house}")
            }
        }

        return if (matchDetails.isNotEmpty()) {
            Triple(true, matchDetails, emptyList())
        } else {
            Triple(false, emptyList(), listOf("No Kendra lord found in Trikona houses"))
        }
    }

    private fun evaluateTrikonaLordsInKendra(node: ConditionNode.TrikonaLordsInKendra): Triple<Boolean, List<String>, List<String>> {
        val kendraHouses = listOf(1, 4, 7, 10)
        val trikonaHouses = listOf(1, 5, 9)
        val matchDetails = mutableListOf<String>()

        for (th in trikonaHouses) {
            val lord = houseLords[th] ?: continue
            val lordPos = planetMap[lord] ?: continue
            if (lordPos.house in kendraHouses) {
                matchDetails.add("Lord of Trikona $th (${lord.displayName}) is in Kendra house ${lordPos.house}")
            }
        }

        return if (matchDetails.isNotEmpty()) {
            Triple(true, matchDetails, emptyList())
        } else {
            Triple(false, emptyList(), listOf("No Trikona lord found in Kendra houses"))
        }
    }
}

// =============================================================================
// VALIDATOR
// =============================================================================

/**
 * Validates [CustomYogaDefinition] objects for correctness before evaluation.
 *
 * Checks for:
 * - Valid house numbers (1-12)
 * - Valid category and strength values
 * - Non-empty condition trees
 * - Logical consistency of conditions
 */
private object AstroDSLValidator {

    private val validCategories = setOf(
        "RAJA_YOGA", "DHANA_YOGA", "SPECIAL_YOGA", "CUSTOM",
        "NABHASA_YOGA", "CHANDRA_YOGA", "SOLAR_YOGA",
        "MAHAPURUSHA_YOGA", "NEGATIVE_YOGA", "BHAVA_YOGA",
        "CONJUNCTION_YOGA", "ARISHTA_YOGA", "PARIVARTTANA_YOGA"
    )

    private val validStrengths = setOf("STRONG", "MODERATE", "WEAK", "VERY_STRONG", "VERY_WEAK", "EXTREMELY_STRONG")

    /**
     * Validate a list of definitions. Returns errors found (empty list if all valid).
     */
    fun validate(definitions: List<CustomYogaDefinition>): List<ParseError> {
        val errors = mutableListOf<ParseError>()

        for (def in definitions) {
            if (def.name.isBlank()) {
                errors.add(ParseError(0, 0, "Yoga definition has empty name", def.source))
            }
            if (def.category.uppercase() !in validCategories) {
                errors.add(ParseError(0, 0, "Invalid category '${def.category}' for yoga '${def.name}'. Valid: $validCategories", def.source))
            }
            if (def.strength.uppercase() !in validStrengths) {
                errors.add(ParseError(0, 0, "Invalid strength '${def.strength}' for yoga '${def.name}'. Valid: $validStrengths", def.source))
            }

            val conditionErrors = validateConditionNode(def.conditions, def.name)
            errors.addAll(conditionErrors)
        }

        return errors
    }

    private fun validateConditionNode(node: ConditionNode, yogaName: String): List<ParseError> {
        val errors = mutableListOf<ParseError>()

        when (node) {
            is ConditionNode.And -> node.conditions.forEach { errors.addAll(validateConditionNode(it, yogaName)) }
            is ConditionNode.Or -> node.conditions.forEach { errors.addAll(validateConditionNode(it, yogaName)) }
            is ConditionNode.Not -> errors.addAll(validateConditionNode(node.condition, yogaName))
            is ConditionNode.PlanetInHouse -> {
                for (h in node.houses) {
                    if (h < 1 || h > 12) {
                        errors.add(ParseError(0, 0, "Invalid house number $h in yoga '$yogaName' (must be 1-12)", ""))
                    }
                }
            }
            is ConditionNode.PlanetAspectsHouse -> {
                if (node.house < 1 || node.house > 12) {
                    errors.add(ParseError(0, 0, "Invalid house number ${node.house} in yoga '$yogaName'", ""))
                }
            }
            is ConditionNode.PlanetIsLordOf -> {
                if (node.house < 1 || node.house > 12) {
                    errors.add(ParseError(0, 0, "Invalid house number ${node.house} in yoga '$yogaName'", ""))
                }
            }
            is ConditionNode.HouseLordInHouse -> {
                if (node.lordOfHouse < 1 || node.lordOfHouse > 12) {
                    errors.add(ParseError(0, 0, "Invalid lordOfHouse ${node.lordOfHouse} in yoga '$yogaName'", ""))
                }
                if (node.inHouse < 1 || node.inHouse > 12) {
                    errors.add(ParseError(0, 0, "Invalid inHouse ${node.inHouse} in yoga '$yogaName'", ""))
                }
            }
            is ConditionNode.HouseLordConjunctHouseLord -> {
                if (node.house1 < 1 || node.house1 > 12 || node.house2 < 1 || node.house2 > 12) {
                    errors.add(ParseError(0, 0, "Invalid house numbers in lord conjunct condition for yoga '$yogaName'", ""))
                }
            }
            is ConditionNode.HouseLordAspectsHouseLord -> {
                if (node.house1 < 1 || node.house1 > 12 || node.house2 < 1 || node.house2 > 12) {
                    errors.add(ParseError(0, 0, "Invalid house numbers in lord aspect condition for yoga '$yogaName'", ""))
                }
            }
            is ConditionNode.HouseHasBenefics -> {
                if (node.house < 1 || node.house > 12) {
                    errors.add(ParseError(0, 0, "Invalid house ${node.house} in HouseHasBenefics for yoga '$yogaName'", ""))
                }
            }
            is ConditionNode.HouseHasNoMalefics -> {
                if (node.house < 1 || node.house > 12) {
                    errors.add(ParseError(0, 0, "Invalid house ${node.house} in HouseHasNoMalefics for yoga '$yogaName'", ""))
                }
            }
            is ConditionNode.PlanetsInHouseCount -> {
                if (node.house < 1 || node.house > 12) {
                    errors.add(ParseError(0, 0, "Invalid house ${node.house} in PlanetsInHouseCount for yoga '$yogaName'", ""))
                }
                if (node.comparator !in listOf(">=", "<=", "==", ">", "<")) {
                    errors.add(ParseError(0, 0, "Invalid comparator '${node.comparator}' for yoga '$yogaName'", ""))
                }
            }
            // The remaining leaf nodes (PlanetInSign, PlanetAspectsPlanet, etc.) have
            // no house numbers to validate beyond the planet/sign enum constraints
            // which are already enforced at parse time.
            is ConditionNode.PlanetInSign,
            is ConditionNode.PlanetAspectsPlanet,
            is ConditionNode.PlanetConjunctPlanet,
            is ConditionNode.PlanetIsExalted,
            is ConditionNode.PlanetIsDebilitated,
            is ConditionNode.PlanetIsRetrograde,
            is ConditionNode.PlanetIsInOwnSign,
            is ConditionNode.KendraLordsInTrikona,
            is ConditionNode.TrikonaLordsInKendra -> { /* no additional validation needed */ }
        }

        return errors
    }
}

// =============================================================================
// MAIN DSL OBJECT (PUBLIC API)
// =============================================================================

/**
 * AstroDSL: Main entry point for the custom yoga definition language.
 *
 * This singleton provides the complete public API for:
 * - Parsing DSL text or JSON into yoga definitions
 * - Validating definitions
 * - Evaluating definitions against a VedicChart
 * - Managing a runtime with multiple definitions
 *
 * **Usage Example (DSL text):**
 * ```kotlin
 * val dsl = """
 *     YOGA "Custom Gajakesari" {
 *         CATEGORY: RAJA_YOGA
 *         STRENGTH: STRONG
 *         CONDITION {
 *             PLANET.JUPITER IN HOUSE.1,4,7,10 FROM PLANET.MOON
 *             AND PLANET.JUPITER IS EXALTED
 *         }
 *         RESULT "Enhanced wisdom, prosperity, and fame through Jupiter's strength"
 *     }
 * """
 * val runtime = AstroDSL.compile(dsl)
 * val results = AstroDSL.evaluate(runtime, chart)
 * ```
 *
 * **Usage Example (JSON):**
 * ```kotlin
 * val json = """
 *     {
 *         "name": "Custom Gajakesari",
 *         "category": "RAJA_YOGA",
 *         "strength": "STRONG",
 *         "conditions": [
 *             {"type": "planet_in_house", "planet": "JUPITER", "houses": [1,4,7,10], "from": "MOON"},
 *             {"type": "planet_not_debilitated", "planet": "JUPITER"}
 *         ],
 *         "logic": "AND",
 *         "result": "Enhanced wisdom, prosperity, and fame"
 *     }
 * """
 * val runtime = AstroDSL.compileJson(json)
 * val results = AstroDSL.evaluate(runtime, chart)
 * ```
 *
 * @author AstroVajra
 */
object AstroDSL {

    /**
     * Compile DSL text source into an [AstroDSLRuntime].
     *
     * The source can contain one or more YOGA { ... } blocks. Parsing errors are collected
     * in the runtime's [AstroDSLRuntime.parseErrors] rather than throwing exceptions,
     * allowing partial compilation of valid definitions.
     *
     * @param source DSL text containing yoga definitions
     * @return Compiled runtime with definitions and any parse errors
     */
    fun compile(source: String): AstroDSLRuntime {
        val tokens = AstroDSLLexer(source).tokenize()
        val (definitions, parseErrors) = AstroDSLParser(tokens).parse()
        val validationErrors = AstroDSLValidator.validate(definitions)
        return AstroDSLRuntime(
            yogaDefinitions = definitions,
            parseErrors = parseErrors + validationErrors
        )
    }

    /**
     * Compile a JSON string (single object or array) into an [AstroDSLRuntime].
     *
     * @param json JSON string containing yoga definition(s)
     * @return Compiled runtime with definitions and any parse errors
     */
    fun compileJson(json: String): AstroDSLRuntime {
        val (definitions, parseErrors) = AstroDSLJsonParser.parse(json)
        val validationErrors = AstroDSLValidator.validate(definitions)
        return AstroDSLRuntime(
            yogaDefinitions = definitions,
            parseErrors = parseErrors + validationErrors
        )
    }

    /**
     * Merge multiple runtimes into a single runtime (e.g., from text + JSON sources).
     *
     * @param runtimes Runtimes to merge
     * @return Combined runtime with all definitions and errors
     */
    fun mergeRuntimes(vararg runtimes: AstroDSLRuntime): AstroDSLRuntime {
        return AstroDSLRuntime(
            yogaDefinitions = runtimes.flatMap { it.yogaDefinitions },
            parseErrors = runtimes.flatMap { it.parseErrors }
        )
    }

    /**
     * Evaluate all yoga definitions in a runtime against a VedicChart.
     *
     * @param runtime The compiled runtime containing yoga definitions
     * @param chart The Vedic chart to evaluate against
     * @return List of results, one per definition, indicating presence and details
     */
    fun evaluate(runtime: AstroDSLRuntime, chart: VedicChart): List<CustomYogaResult> {
        val evaluator = ConditionEvaluator(chart)
        return runtime.yogaDefinitions.map { definition ->
            try {
                val (isPresent, matchDetails, failedConditions) = evaluator.evaluate(definition.conditions)
                CustomYogaResult(
                    definition = definition,
                    isPresent = isPresent,
                    matchDetails = matchDetails,
                    failedConditions = failedConditions
                )
            } catch (e: Exception) {
                CustomYogaResult(
                    definition = definition,
                    isPresent = false,
                    matchDetails = emptyList(),
                    failedConditions = listOf("Evaluation error: ${e.message}")
                )
            }
        }
    }

    /**
     * Evaluate a single custom yoga definition against a VedicChart.
     *
     * @param definition The yoga definition to evaluate
     * @param chart The Vedic chart to evaluate against
     * @return Result indicating presence and match details
     */
    fun evaluateSingle(definition: CustomYogaDefinition, chart: VedicChart): CustomYogaResult {
        val evaluator = ConditionEvaluator(chart)
        return try {
            val (isPresent, matchDetails, failedConditions) = evaluator.evaluate(definition.conditions)
            CustomYogaResult(
                definition = definition,
                isPresent = isPresent,
                matchDetails = matchDetails,
                failedConditions = failedConditions
            )
        } catch (e: Exception) {
            CustomYogaResult(
                definition = definition,
                isPresent = false,
                matchDetails = emptyList(),
                failedConditions = listOf("Evaluation error: ${e.message}")
            )
        }
    }

    /**
     * Evaluate all definitions in a runtime and return only the yogas that are present.
     *
     * @param runtime The compiled runtime
     * @param chart The Vedic chart to evaluate against
     * @return List of results where [CustomYogaResult.isPresent] is true
     */
    fun evaluatePresent(runtime: AstroDSLRuntime, chart: VedicChart): List<CustomYogaResult> {
        return evaluate(runtime, chart).filter { it.isPresent }
    }

    /**
     * Quick check: does any yoga in the runtime match the given chart?
     *
     * @param runtime The compiled runtime
     * @param chart The Vedic chart to evaluate against
     * @return true if at least one custom yoga is present
     */
    fun hasAnyMatch(runtime: AstroDSLRuntime, chart: VedicChart): Boolean {
        val evaluator = ConditionEvaluator(chart)
        return runtime.yogaDefinitions.any { definition ->
            try {
                evaluator.evaluate(definition.conditions).first
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Validate a runtime for errors without evaluating against a chart.
     *
     * @param runtime The runtime to validate
     * @return List of validation errors (empty if all definitions are valid)
     */
    fun validate(runtime: AstroDSLRuntime): List<ParseError> {
        return AstroDSLValidator.validate(runtime.yogaDefinitions)
    }

    /**
     * Built-in library of classical yoga definitions in DSL format.
     *
     * Contains a curated set of well-known yogas defined in the DSL for
     * reference and immediate use. These complement the existing YogaCalculator
     * evaluators and serve as examples for the DSL syntax.
     *
     * @return Compiled runtime with built-in yoga definitions
     */
    fun builtInYogaLibrary(): AstroDSLRuntime {
        return compile(BUILT_IN_YOGA_DEFINITIONS)
    }

    // =============================================================================
    // BUILT-IN YOGA DEFINITIONS
    // =============================================================================

    private val BUILT_IN_YOGA_DEFINITIONS = """
        // ============================================================
        // Classical Yoga Definitions in AstroDSL Format
        // Based on BPHS, Phaladeepika, and Saravali
        // ============================================================

        YOGA "Gajakesari Yoga (DSL)" {
            CATEGORY: RAJA_YOGA
            STRENGTH: STRONG
            CONDITION {
                PLANET.JUPITER IN HOUSE.1,4,7,10
            }
            RESULT "Jupiter in Kendra from Lagna bestows wisdom, fame, prosperity, and leadership. The native commands respect and has a generous disposition."
        }

        YOGA "Hamsa Yoga (DSL)" {
            CATEGORY: RAJA_YOGA
            STRENGTH: STRONG
            CONDITION {
                PLANET.JUPITER IN HOUSE.1,4,7,10 AND PLANET.JUPITER IS IN_OWN_SIGN
            }
            RESULT "Jupiter in Kendra in own sign forms Hamsa Mahapurusha Yoga. The native is virtuous, learned, and earns fame through righteous actions."
        }

        YOGA "Malavya Yoga (DSL)" {
            CATEGORY: RAJA_YOGA
            STRENGTH: STRONG
            CONDITION {
                PLANET.VENUS IN HOUSE.1,4,7,10 AND PLANET.VENUS IS IN_OWN_SIGN
            }
            RESULT "Venus in Kendra in own sign forms Malavya Mahapurusha Yoga. The native enjoys luxury, beauty, artistic talent, and conjugal bliss."
        }

        YOGA "Ruchaka Yoga (DSL)" {
            CATEGORY: RAJA_YOGA
            STRENGTH: STRONG
            CONDITION {
                PLANET.MARS IN HOUSE.1,4,7,10 AND PLANET.MARS IS IN_OWN_SIGN
            }
            RESULT "Mars in Kendra in own sign forms Ruchaka Mahapurusha Yoga. The native is courageous, commanding, and achieves success through valor."
        }

        YOGA "Sasa Yoga (DSL)" {
            CATEGORY: RAJA_YOGA
            STRENGTH: STRONG
            CONDITION {
                PLANET.SATURN IN HOUSE.1,4,7,10 AND PLANET.SATURN IS IN_OWN_SIGN
            }
            RESULT "Saturn in Kendra in own sign forms Sasa Mahapurusha Yoga. The native wields authority, leads communities, and demonstrates discipline."
        }

        YOGA "Bhadra Yoga (DSL)" {
            CATEGORY: RAJA_YOGA
            STRENGTH: STRONG
            CONDITION {
                PLANET.MERCURY IN HOUSE.1,4,7,10 AND PLANET.MERCURY IS IN_OWN_SIGN
            }
            RESULT "Mercury in Kendra in own sign forms Bhadra Mahapurusha Yoga. The native is learned, eloquent, skilled in commerce, and intellectually gifted."
        }

        YOGA "Lakshmi Yoga (DSL)" {
            CATEGORY: DHANA_YOGA
            STRENGTH: STRONG
            CONDITION {
                LORD_OF HOUSE.9 IN HOUSE.1,4,7,10 AND LORD_OF HOUSE.1 IN HOUSE.1,5,9
            }
            RESULT "Lord of 9th in Kendra and Lagna lord in Dharma Trikona creates Lakshmi Yoga. Bestows great wealth, fortune, and divine blessings."
        }

        YOGA "Dhana Yoga - 2nd and 11th Lords (DSL)" {
            CATEGORY: DHANA_YOGA
            STRENGTH: MODERATE
            CONDITION {
                LORD_OF HOUSE.2 CONJUNCT LORD_OF HOUSE.11
            }
            RESULT "Conjunction of 2nd and 11th house lords creates Dhana Yoga. Indicates strong capacity for wealth accumulation and financial gains."
        }

        YOGA "Raja Yoga - 9th and 10th Exchange (DSL)" {
            CATEGORY: RAJA_YOGA
            STRENGTH: STRONG
            CONDITION {
                LORD_OF HOUSE.9 IN HOUSE.10 AND LORD_OF HOUSE.10 IN HOUSE.9
            }
            RESULT "Exchange between 9th and 10th lords creates a powerful Raja Yoga. The native rises to positions of power through righteous means and destiny's favor."
        }

        YOGA "Viparita Raja Yoga - 6th Lord (DSL)" {
            CATEGORY: RAJA_YOGA
            STRENGTH: MODERATE
            CONDITION {
                LORD_OF HOUSE.6 IN HOUSE.8 OR LORD_OF HOUSE.6 IN HOUSE.12
            }
            RESULT "Lord of 6th in 8th or 12th house creates Viparita Raja Yoga. Obstacles transform into unexpected success and rise through adversity."
        }

        YOGA "Neecha Bhanga Raja Yoga - Jupiter (DSL)" {
            CATEGORY: SPECIAL_YOGA
            STRENGTH: STRONG
            CONDITION {
                PLANET.JUPITER IS DEBILITATED AND PLANET.SATURN IN HOUSE.1,4,7,10
            }
            RESULT "Debilitated Jupiter with Saturn in Kendra creates Neecha Bhanga Raja Yoga. Initial setbacks in wisdom/fortune are overcome, leading to extraordinary achievements."
        }

        YOGA "Budhaditya Yoga (DSL)" {
            CATEGORY: SPECIAL_YOGA
            STRENGTH: MODERATE
            CONDITION {
                PLANET.SUN CONJUNCT PLANET.MERCURY AND HOUSE.1 HAS_NO_MALEFICS
            }
            RESULT "Sun-Mercury conjunction with an unafflicted Lagna forms Budhaditya Yoga. The native possesses sharp intellect, academic prowess, and communication skills."
        }

        YOGA "Amala Yoga (DSL)" {
            CATEGORY: SPECIAL_YOGA
            STRENGTH: MODERATE
            CONDITION {
                HOUSE.10 HAS_BENEFICS AND HOUSE.10 HAS_NO_MALEFICS
            }
            RESULT "Only benefics in the 10th house form Amala Yoga. The native achieves a spotless reputation and lasting fame through virtuous conduct in career."
        }

        YOGA "Kendra-Trikona Raja Yoga (DSL)" {
            CATEGORY: RAJA_YOGA
            STRENGTH: STRONG
            CONDITION {
                KENDRA_LORDS IN TRIKONA AND TRIKONA_LORDS IN KENDRA
            }
            RESULT "Mutual placement of Kendra and Trikona lords creates a powerful Raja Yoga. The native attains authority, fame, and wealth through both destiny and effort."
        }
    """.trimIndent()
}
