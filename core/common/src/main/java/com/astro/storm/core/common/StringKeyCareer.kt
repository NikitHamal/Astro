package com.astro.storm.core.common

/**
 * Career and Profession Localization Keys
 */
enum class StringKeyCareer(override val en: String, override val ne: String) : StringKeyInterface {
    CAT_GOV_ADMIN("Government & Administration", "सरकारी र प्रशासन"),
    CAT_MILITARY_POLICE("Military & Police", "सेना र प्रहरी"),
    CAT_MEDICINE_HEALTHCARE("Medicine & Healthcare", "चिकित्सा र स्वास्थ्य"),
    CAT_LAW_JUDICIARY("Law & Judiciary", "कानून र न्यायपालिका"),
    CAT_EDUCATION_TEACHING("Education & Teaching", "शिक्षा र शिक्षण"),
    CAT_FINANCE_BANKING("Finance & Banking", "वित्त र बैंकिङ"),
    CAT_TECHNOLOGY_IT("Technology & IT", "प्रविधि र आईटी"),
    CAT_ARTS_ENTERTAINMENT("Arts & Entertainment", "कला र मनोरञ्जन"),
    CAT_MEDIA_JOURNALISM("Media & Journalism", "मिडिया र पत्रकारिता"),
    CAT_BUSINESS_COMMERCE("Business & Commerce", "व्यापार र वाणिज्य"),
    CAT_ENGINEERING_TECHNICAL("Engineering & Technical", "इन्जिनियरिङ र प्राविधिक"),
    CAT_SCIENCE_RESEARCH("Science & Research", "विज्ञान र अनुसन्धान"),
    CAT_HOSPITALITY_SERVICE("Hospitality & Service", "आतिथ्य र सेवा"),
    CAT_REAL_ESTATE_CONSTRUCTION("Real Estate & Construction", "घरजग्गा र निर्माण"),
    CAT_AGRICULTURE_FARMING("Agriculture & Farming", "कृषि र खेती"),
    CAT_SPIRITUALITY_RELIGIOUS("Spirituality & Religious", "आध्यात्मिकता र धार्मिक"),
    CAT_SPORTS_FITNESS("Sports & Fitness", "खेलकुद र फिटनेस"),
    CAT_TRANSPORTATION_LOGISTICS("Transportation & Logistics", "यातायात र रसद"),
    CAT_FOREIGN_SERVICES("Foreign Services", "परराष्ट्र सेवा"),
    CAT_FREELANCE_CONSULTING("Freelance & Consulting", "फ्रिल्यान्स र परामर्श"),
}
