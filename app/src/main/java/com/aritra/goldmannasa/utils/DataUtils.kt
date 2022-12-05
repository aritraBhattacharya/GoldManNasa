package com.aritra.goldmannasa.utils

import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.dtos.APODDto


fun APODDto.toEntity() = APODEntity(
    copyright = copyright ?: "",
    date = date,
    explanation = explanation,
    hdurl = hdurl,
    media_type = media_type,
    service_version = service_version,
    title = title,
    url = url,
    isFavourite = false
)