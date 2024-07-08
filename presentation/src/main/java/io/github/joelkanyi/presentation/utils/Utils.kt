package io.github.joelkanyi.presentation.utils

fun String.toISO3166Alpha2(): String {
    return when (this.lowercase()) {
        "kenya" -> "ke"
        "united states" -> "us"
        "united kingdom" -> "gb"
        "australia" -> "au"
        "canada" -> "ca"
        "india" -> "in"
        "germany" -> "de"
        "france" -> "fr"
        "italy" -> "it"
        "netherlands" -> "nl"
        "norway" -> "no"
        "sweden" -> "se"
        "china" -> "cn"
        "japan" -> "jp"
        "south korea" -> "kr"
        "russia" -> "ru"
        "brazil" -> "br"
        "argentina" -> "ar"
        "mexico" -> "mx"
        "south africa" -> "za"
        "nigeria" -> "ng"
        "egypt" -> "eg"
        "saudi arabia" -> "sa"
        "united arab emirates" -> "ae"
        "kuwait" -> "kw"
        else -> ""
    }
}