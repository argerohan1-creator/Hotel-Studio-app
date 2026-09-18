package com.example.util

import java.util.Locale

object SalutationHelper {

    private val TITLE_PREFIX_REGEX = Regex("^(mr\\.?|ms\\.?|mrs\\.?|miss|dr\\.?|doctor|prof\\.?|professor|chef|capt\\.?|captain|sir|lady|rev\\.?|hon\\.?)\\s+", RegexOption.IGNORE_CASE)

    private val FEMALE_NAMES = setOf(
        // Western
        "emily", "sarah", "jessica", "jennifer", "amanda", "melissa", "laura", "stephanie",
        "nicole", "mary", "patricia", "linda", "barbara", "elizabeth", "susan", "margaret",
        "dorothy", "lisa", "nancy", "karen", "betty", "helen", "sandra", "donna", "carol",
        "ruth", "sharon", "michelle", "kimberly", "deborah", "cynthia", "amy", "angela",
        "emma", "olivia", "sophia", "isabella", "ava", "mia", "charlotte", "amelia",
        "harper", "evelyn", "abigail", "ella", "scarlett", "grace", "chloe", "victoria",
        "riley", "aria", "lily", "aubrey", "zoey", "penelope", "hannah", "layla", "nora",
        "elena", "claire", "anna", "audrey", "alyssa", "alice", "sophie", "lucy", "megan",
        "clara", "julia", "rachel", "rebecca", "natalie", "lauren", "katherine", "katie",
        "catherine", "diana", "christine", "maria", "teresa", "monica", "vanessa", "valeria",
        "andrea", "stephanie", "tiffany", "amber", "courtney", "danielle", "chelsea", "brittany",
        
        // Indian / South Asian
        "priya", "pooja", "neha", "ananya", "deepa", "sneha", "sunita", "kavita", "swati",
        "ritu", "anita", "divya", "shreya", "tanvi", "aditi", "meera", "deepika", "priyanka",
        "shruti", "suman", "rekha", "preeti", "geeta", "radha", "lakshmi", "saraswati",
        "parvati", "nisha", "simran", "manpreet", "gurpreet", "aarti", "payal", "komal",
        "riya", "ishita", "anjali", "sonam", "jyoti", "vandana", "sangeeta", "shweta",
        "archana", "poonam", "rashmi", "sonal", "pallavi", "vidya", "shilpa", "roshni",
        "mamta", "renu", "seema", "alka", "jaya", "madhu", "usha", "rekha", "pratibha"
    )

    private val MALE_NAMES = setOf(
        // Western
        "james", "john", "robert", "michael", "william", "david", "richard", "joseph",
        "thomas", "charles", "christopher", "daniel", "matthew", "anthony", "mark",
        "donald", "steven", "paul", "andrew", "joshua", "kenneth", "kevin", "brian",
        "george", "timothy", "ronald", "edward", "jason", "jeffrey", "ryan", "jacob",
        "gary", "nicholas", "eric", "jonathan", "stephen", "larry", "justin", "scott",
        "brandon", "benjamin", "samuel", "gregory", "alexander", "alex", "frank", "patrick",
        "raymond", "jack", "dennis", "jerry", "tyler", "aaron", "jose", "adam", "nathan",
        "henry", "douglas", "zachary", "peter", "kyle", "walter", "harold", "jeremy",
        "ethan", "carl", "keith", "roger", "gerald", "christian", "terry", "sean", "arthur",
        "austin", "noah", "liam", "oliver", "lucas", "mason", "logan", "elijah", "aiden",
        
        // Indian / South Asian
        "rohan", "rahul", "amit", "vikram", "suresh", "ramesh", "ajay", "arjun", "aditya",
        "vivek", "sanjay", "rajesh", "manoj", "anil", "sunil", "deepak", "sachin", "rohit",
        "alok", "gaurav", "mayank", "abhishek", "varun", "kunal", "manish", "pankaj",
        "sandeep", "pradeep", "rakesh", "mukesh", "dinesh", "mahesh", "naresh", "harish",
        "ashok", "vinod", "pramod", "santosh", "satish", "naveen", "praveen", "rajiv",
        "sanjeev", "vijay", "raj", "aman", "karan", "kabir", "aryan", "dev", "yash",
        "krishna", "ram", "shiva", "shyam", "mohan", "karthik", "venkat", "balaji", "subhash"
    )

    /**
     * Determines whether the given string has an existing title prefix.
     */
    fun extractTitle(input: String): String? {
        val trimmed = input.trim()
        val lower = trimmed.lowercase(Locale.ROOT)
        return when {
            lower.startsWith("mr.") || lower.startsWith("mr ") -> "Mr."
            lower.startsWith("mrs.") || lower.startsWith("mrs ") -> "Mrs."
            lower.startsWith("ms.") || lower.startsWith("ms ") || lower.startsWith("miss ") -> "Ms."
            lower.startsWith("dr.") || lower.startsWith("dr ") || lower.startsWith("doctor ") -> "Dr."
            lower.startsWith("prof.") || lower.startsWith("prof ") || lower.startsWith("professor ") -> "Prof."
            lower.startsWith("chef ") || lower.startsWith("chef.") -> "Chef"
            lower.startsWith("capt.") || lower.startsWith("capt ") || lower.startsWith("captain ") -> "Capt."
            lower.startsWith("rev.") || lower.startsWith("rev ") -> "Rev."
            lower.startsWith("hon.") || lower.startsWith("hon ") -> "Hon."
            lower.startsWith("sir ") -> "Sir"
            lower.startsWith("lady ") -> "Lady"
            else -> null
        }
    }

    /**
     * Strips leading title from the name string.
     */
    fun cleanName(input: String): String {
        return input.trim().replace(TITLE_PREFIX_REGEX, "").trim()
    }

    /**
     * Automatically picks up the most appropriate salutation based on the name
     * and optionally designation.
     */
    fun detectSalutation(rawName: String, designation: String = ""): String {
        val nameTrimmed = rawName.trim()
        if (nameTrimmed.isBlank()) return "Mr."

        // 1. Direct title in name
        val explicitTitle = extractTitle(nameTrimmed)
        if (explicitTitle != null) {
            return explicitTitle
        }

        // 2. Designation keywords
        val desigLower = designation.lowercase(Locale.ROOT)
        if (desigLower.contains("doctor") || desigLower.contains("physician") || 
            desigLower.contains("surgeon") || desigLower.contains("phd") || 
            desigLower.contains("md") || desigLower.contains("dentist")) {
            return "Dr."
        }
        if (desigLower.contains("chef") || desigLower.contains("culinary")) {
            return "Chef"
        }
        if (desigLower.contains("professor") || desigLower.contains("dean") || 
            desigLower.contains("faculty") || desigLower.contains("lecturer")) {
            return "Prof."
        }
        if (desigLower.contains("captain") || desigLower.contains("pilot") || 
            desigLower.contains("commander")) {
            return "Capt."
        }

        // 3. Extract first name
        val cleanName = cleanName(nameTrimmed)
        val firstName = cleanName.split(Regex("\\s+")).firstOrNull()?.lowercase(Locale.ROOT) ?: ""
        if (firstName.isBlank()) return "Mr."

        // Exact match in female dictionary
        if (FEMALE_NAMES.contains(firstName)) {
            return "Ms."
        }

        // Exact match in male dictionary
        if (MALE_NAMES.contains(firstName)) {
            return "Mr."
        }

        // Morphological heuristics
        if (firstName.endsWith("a") || firstName.endsWith("i") || firstName.endsWith("ee") ||
            firstName.endsWith("ia") || firstName.endsWith("na") || firstName.endsWith("ne") ||
            firstName.endsWith("ette") || firstName.endsWith("ine") || firstName.endsWith("elle")) {
            return "Ms."
        }

        // Default to Mr.
        return "Mr."
    }

    /**
     * Formats the final printable name line.
     */
    fun formatDisplay(salutation: String, rawName: String): String {
        val clean = cleanName(rawName)
        if (clean.isBlank()) return ""
        return if (salutation.equals("None", ignoreCase = true) || salutation.isBlank()) {
            clean
        } else {
            "$salutation $clean"
        }
    }
}
