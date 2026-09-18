package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import com.example.ui.theme.PlayfairSerif
import com.example.ui.theme.MontserratSans
import androidx.compose.ui.text.font.FontStyle
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DishEntity
import com.example.ui.theme.AllergenIcon
import com.example.ui.theme.FssaiRegulatoryMark
import com.example.ui.theme.TemplateSkin

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuffetTagCard(
    dish: DishEntity,
    skin: TemplateSkin,
    modifier: Modifier = Modifier,
    isMasterPreview: Boolean = true,
    customLogoBase64: String? = null,
    onClick: (() -> Unit)? = null
) {
    val cardModifier = modifier
        .shadow(
            elevation = if (isMasterPreview) 12.dp else 4.dp,
            shape = RoundedCornerShape(10.dp)
        )
        .clip(RoundedCornerShape(10.dp))
        .background(skin.backgroundColor)
        .then(
            if (skin.isDoubleBorder) {
                Modifier
                    .border(skin.borderWidth, skin.borderColor, RoundedCornerShape(10.dp))
                    .padding(3.dp)
                    .border(1.dp, skin.borderColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            } else {
                Modifier.border(skin.borderWidth, skin.borderColor, RoundedCornerShape(10.dp))
            }
        )
        .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
        .padding(if (isMasterPreview) 20.dp else 14.dp)

    Box(modifier = cardModifier) {
        // FSSAI Veg / Non-Veg badge at top-left
        FssaiRegulatoryMark(
            isVeg = dish.type.lowercase() == "veg",
            modifier = Modifier.align(Alignment.TopStart),
            size = if (isMasterPreview) 16.dp else 12.dp
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Organization Logo / Header
            if (!customLogoBase64.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .padding(bottom = if (isMasterPreview) 14.dp else 8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (skin.isDark) Color.White else Color.Transparent)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    AsyncImage(
                        model = customLogoBase64,
                        contentDescription = "Establishment Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .heightIn(max = if (isMasterPreview) 40.dp else 30.dp)
                            .fillMaxWidth(0.6f)
                    )
                }
            } else {
                Text(
                    text = "HOTEL STUDIO",
                    fontFamily = PlayfairSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isMasterPreview) 13.sp else 10.sp,
                    letterSpacing = 2.sp,
                    color = if (skin.isDark) skin.borderColor else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = if (isMasterPreview) 12.dp else 6.dp)
                )
            }

            // Food Name
            Text(
                text = dish.name.uppercase(),
                fontFamily = PlayfairSerif,
                fontWeight = FontWeight.Bold,
                fontSize = if (isMasterPreview) 19.sp else 13.5.sp,
                lineHeight = if (isMasterPreview) 24.sp else 17.sp,
                letterSpacing = 0.5.sp,
                color = skin.textColor,
                textAlign = TextAlign.Center,
                maxLines = if (isMasterPreview) 3 else 2,
                overflow = TextOverflow.Ellipsis
            )

            // Description
            if (dish.desc.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dish.desc,
                    fontFamily = MontserratSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = if (isMasterPreview) 13.sp else 10.5.sp,
                    lineHeight = if (isMasterPreview) 18.sp else 14.sp,
                    color = skin.secondaryTextColor,
                    textAlign = TextAlign.Center,
                    maxLines = if (isMasterPreview) 3 else 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Calories
            if (dish.cals.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = dish.cals,
                    fontFamily = MontserratSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = if (isMasterPreview) 11.5.sp else 9.sp,
                    letterSpacing = 0.3.sp,
                    color = skin.secondaryTextColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dietary & Allergens Section
            if (dish.allergens.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 6.dp),
                    thickness = 0.8.dp,
                    color = skin.borderColor.copy(alpha = 0.35f)
                )

                DietaryBadgeRow(
                    tags = dish.allergens,
                    isCompact = !isMasterPreview,
                    maxTags = if (isMasterPreview) 8 else 5
                )
            }

            // Disclaimer
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please inform the server for any food allergies",
                fontStyle = FontStyle.Italic,
                fontSize = if (isMasterPreview) 8.5.sp else 7.sp,
                color = skin.secondaryTextColor.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )
        }
    }
}
