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
            bio = "Tumatawid ng bundok papasok school T_T",
            imageRes = R.drawable.faith
        ),
        Member(
            name = "John Paul Valenzuela",
            position = "Integrated Systems Developer",
            bio = "taong grasa ng bagong silang",
            imageRes = R.drawable.john
        ),
        Member(
            name = "Julliah Nicole Paran",
            position = "Quality Assurance",
            bio = "Mga taga bulacan talaga haaystt",
            imageRes = R.drawable.paran
        ),
        Member(
            name = "ChristDale Esteban",
            position = "Technical Assistant",
            bio = "heartthrob ng Commonwealth",
            imageRes = R.drawable.elad
        ),
    )
}