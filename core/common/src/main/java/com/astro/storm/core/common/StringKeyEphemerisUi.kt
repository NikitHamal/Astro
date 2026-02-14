package com.astro.storm.core.common

enum class StringKeyEphemerisUi(override val en: String, override val ne: String) : StringKeyInterface {
    HEADER_TITLE("Ephemeris", "इफेमेरिस"),
    HEADER_SUBTITLE("Live Celestial Movements", "प्रत्यक्ष आकाशीय गतिहरू"),
    DAY_OFFSET_LABEL("+%dD", "+%d दिन"),

    TRANSIT_PERIOD_FALLBACK("Transit period", "गोचर अवधि"),
    INTENSITY_LABEL("Intensity %d/5", "तीव्रता %d/5"),
    MOTION_REVERSED("Motion Reversed", "उल्टो गति"),

    ASPECTING_TEMPLATE("%1\$s aspecting %2\$s", "%1\$s को %2\$s मा दृष्टि"),
    ORB_LABEL("Orb %d°", "अंश दूरी %d°"),
    IN_TEMPLATE("%1\$s in %2\$s", "%1\$s %2\$s मा"),

    STATUS_EXALTED("Exalted", "उच्च"),
    STATUS_DEBILITATED("Debilitated", "नीच"),
    STATUS_OWN_SIGN("Own Sign", "स्व-क्षेत्र"),

    FALLBACK_TRANSIT_TOKEN("TR", "गो");
}
