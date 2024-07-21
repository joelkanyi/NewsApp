/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.data.utils

import io.github.joelkanyi.domain.model.News
import java.util.concurrent.atomic.AtomicInteger

class NewsFactory {
    private val counter = AtomicInteger(0)

    fun createNews(): News {
        val id = counter.incrementAndGet()
        val news =
            News(
                author = "author $id",
                content = "content $id",
                description = "description $id",
                publishedAt = "publishedAt $id",
                source = "source $id",
                title = "title $id",
                url = "url $id",
                imageUrl = "imageUrl $id"
            )
        return news
    }
}
