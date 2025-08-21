package com.example.feature.aboutUs

import androidx.lifecycle.ViewModel
import com.example.domain.model.Member
import javax.inject.Inject
import com.example.feature.R

class AboutUsPageViewModel @Inject constructor(

): ViewModel() {

    val members = listOf(
        Member(
            name = "Faith Bianca Madarang",
            position = "Project Manager",
            bio = "An idea isn't responsible for the people who believe in it.",
            imageRes = R.drawable.faith
        ),
        Member(
            name = "John Paul Valenzuela",
            position = "Integrated Systems Developer",
            bio = "If two wrongs don't make a right, try three.",
            imageRes = R.drawable.john
        ),
        Member(
            name = "Julliah Nicole Paran",
            position = "Quality Assurance",
            bio = "Better late than never, but never late is better",
            imageRes = R.drawable.paran
        ),
        Member(
            name = "ChristDale Esteban",
            position = "Technical Assistant",
            bio = "Behind every great man is a woman rolling her eyes.",
            imageRes = R.drawable.elad
        ),
    )
}