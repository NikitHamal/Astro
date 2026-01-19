package com.astro.storm.core.common

/**
 * Supplemental localization keys for native analysis traits and labels.
 */
enum class StringKeyNativeTraits(override val en: String, override val ne: String) : StringKeyInterface {
    // General
    LABEL_UNKNOWN("Unknown", "अज्ञात"),
    LABEL_DIGNITY("Dignity", "गरिमा"),
    LABEL_HOUSE_NUMBER_FMT("%dth", "%dऔं"),
    LABEL_ELEMENT("Element", "तत्व"),
    LABEL_MODALITY("Modality", "स्वभाव"),
    LABEL_CONSTITUTION("Constitution", "संरचना"),
    LABEL_LORD("Lord", "स्वामी"),

    // Element labels
    ELEMENT_FIRE("Fire", "अग्नि"),
    ELEMENT_EARTH("Earth", "पृथ्वी"),
    ELEMENT_AIR("Air", "वायु"),
    ELEMENT_WATER("Water", "जल"),

    // Modality labels
    MODALITY_CARDINAL("Cardinal (Chara)", "चर"),
    MODALITY_FIXED("Fixed (Sthira)", "स्थिर"),
    MODALITY_MUTABLE("Mutable (Dwiswabhava)", "द्विस्वभाव"),

    // Constitution labels
    CONSTITUTION_STRONG("Strong", "बलियो"),
    CONSTITUTION_MODERATE("Moderate", "मध्यम"),
    CONSTITUTION_SENSITIVE("Sensitive", "संवेदनशील"),

    // Longevity labels
    LONGEVITY_LONG("Long Life", "दीर्घ आयु"),
    LONGEVITY_MEDIUM("Medium Life", "मध्यम आयु"),
    LONGEVITY_REQUIRES_CARE("Requires Care", "हेरचाह आवश्यक"),

    // Dignity labels
    DIGNITY_EXALTED("Exalted", "उच्च"),
    DIGNITY_MOOLATRIKONA("Moolatrikona", "मूलत्रिकोण"),
    DIGNITY_OWN_SIGN("Own Sign", "स्वगृह"),
    DIGNITY_FRIEND_SIGN("Friend's Sign", "मित्र राशि"),
    DIGNITY_NEUTRAL_SIGN("Neutral Sign", "सम राशि"),
    DIGNITY_ENEMY_SIGN("Enemy's Sign", "शत्रु राशि"),
    DIGNITY_DEBILITATED("Debilitated", "नीच"),

    // Spouse traits
    SPOUSE_TRAIT_DYNAMIC("dynamic", "गतिशील"),
    SPOUSE_TRAIT_PRACTICAL("practical", "व्यावहारिक"),
    SPOUSE_TRAIT_INTELLECTUAL("intellectual", "बौद्धिक"),
    SPOUSE_TRAIT_CARING("caring", "हेरचाह गर्ने"),
    SPOUSE_TRAIT_ARTISTIC("artistic", "कलात्मक"),
    SPOUSE_TRAIT_AMBITIOUS("ambitious", "महत्त्वाकांक्षी"),
    SPOUSE_TRAIT_SPIRITUAL("spiritual", "आध्यात्मिक"),
    SPOUSE_TRAIT_DISCIPLINED("disciplined", "अनुशासित"),
    SPOUSE_TRAIT_COMPASSIONATE("compassionate", "करुणामय"),
    SPOUSE_TRAIT_INDEPENDENT("independent", "स्वतन्त्र"),
    SPOUSE_TRAIT_ADAPTABILITY("adaptable", "अनुकूलनशील"),

    // Career fields
    CAREER_FIELD_GOVERNMENT("Government & Administration", "सरकार र प्रशासन"),
    CAREER_FIELD_LEADERSHIP("Leadership & Management", "नेतृत्व र व्यवस्थापन"),
    CAREER_FIELD_LAW("Law & Judiciary", "कानून र न्यायपालिका"),
    CAREER_FIELD_EDUCATION("Education & Training", "शिक्षा र प्रशिक्षण"),
    CAREER_FIELD_FINANCE("Finance & Banking", "वित्त र बैंकिङ"),
    CAREER_FIELD_TECHNOLOGY("Technology & Engineering", "प्रविधि र इन्जिनियरिङ"),
    CAREER_FIELD_HEALTHCARE("Healthcare & Healing", "स्वास्थ्य सेवा र उपचार"),
    CAREER_FIELD_CREATIVE("Creative Arts & Media", "रचनात्मक कला र मिडिया"),
    CAREER_FIELD_COMMERCE("Business & Commerce", "व्यापार र वाणिज्य"),
    CAREER_FIELD_AGRICULTURE("Agriculture & Land", "कृषि र भूमि"),
    CAREER_FIELD_RESEARCH("Research & Investigation", "अनुसन्धान र अन्वेषण"),
    CAREER_FIELD_HOSPITALITY("Hospitality & Tourism", "आतिथ्य र पर्यटन"),
    CAREER_FIELD_DEFENSE("Defense & Security", "रक्षा र सुरक्षा"),
    CAREER_FIELD_PUBLIC_SERVICE("Public Service", "सार्वजनिक सेवा"),
    CAREER_FIELD_SPIRITUAL("Spiritual Guidance", "आध्यात्मिक मार्गदर्शन"),

    // Wealth sources
    WEALTH_SOURCE_SALARY("Stable salary income", "स्थिर तलब आय"),
    WEALTH_SOURCE_BUSINESS("Business profits", "व्यवसायिक लाभ"),
    WEALTH_SOURCE_INVESTMENT("Investments and assets", "लगानी र सम्पत्ति"),
    WEALTH_SOURCE_PROPERTY("Property and land", "सम्पत्ति र भूमि"),
    WEALTH_SOURCE_FOREIGN("Foreign or distant income", "विदेशी वा दूरगामी आय"),
    WEALTH_SOURCE_INHERITANCE("Family inheritance", "पारिवारिक उत्तराधिकार"),
    WEALTH_SOURCE_ADVISORY("Advisory and consulting", "सल्लाहकार र परामर्श"),

    // Education subjects
    EDUCATION_SUBJECT_SCIENCE("Science & Technology", "विज्ञान र प्रविधि"),
    EDUCATION_SUBJECT_MANAGEMENT("Management & Strategy", "व्यवस्थापन र रणनीति"),
    EDUCATION_SUBJECT_HUMANITIES("Humanities & Languages", "मानविकी र भाषा"),
    EDUCATION_SUBJECT_MEDICINE("Medicine & Health Sciences", "चिकित्सा र स्वास्थ्य विज्ञान"),
    EDUCATION_SUBJECT_LAW("Law & Public Policy", "कानून र सार्वजनिक नीति"),
    EDUCATION_SUBJECT_COMMERCE("Commerce & Accounting", "वाणिज्य र लेखा"),
    EDUCATION_SUBJECT_ARTS("Arts & Design", "कला र डिजाइन"),
    EDUCATION_SUBJECT_SPIRITUAL("Spiritual Studies", "आध्यात्मिक अध्ययन"),
    EDUCATION_SUBJECT_MATHEMATICS("Mathematics & Analytics", "गणित र विश्लेषण"),

    // Spiritual practices
    SPIRITUAL_PRACTICE_MEDITATION("Meditation and mindfulness", "ध्यान र सजगता"),
    SPIRITUAL_PRACTICE_MANTRA("Mantra recitation", "मन्त्र जप"),
    SPIRITUAL_PRACTICE_PILGRIMAGE("Pilgrimage and sacred travel", "तीर्थ यात्रा"),
    SPIRITUAL_PRACTICE_SEVA("Seva and compassionate service", "सेवा र करुणामय कर्म"),
    SPIRITUAL_PRACTICE_STUDY("Scriptural study", "शास्त्रीय अध्ययन"),
    SPIRITUAL_PRACTICE_YOGA("Yoga and pranayama", "योग र प्राणायाम"),

    // Health concerns
    HEALTH_CONCERN_STRESS("stress and nervous strain", "तनाव र स्नायु तनाव"),
    HEALTH_CONCERN_DIGESTION("digestive sensitivity", "पाचन संवेदनशीलता"),
    HEALTH_CONCERN_HEART("heart and circulatory care", "हृदय र रक्तसञ्चार हेरचाह"),
    HEALTH_GENERAL_CARE("general health maintenance", "सामान्य स्वास्थ्य हेरचाह")
}
