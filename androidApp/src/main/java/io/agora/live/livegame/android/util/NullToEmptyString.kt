package io.agora.live.livegame.android.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

object NullToEmptyString {
    @FromJson
    fun fromJson(reader: JsonReader): String {
        if (reader.peek() != JsonReader.Token.NULL)
            return reader.nextString()
        // 消耗掉 Null
        reader.nextNull<Unit>()
        return ""
    }
}