package org.chorus_oss.varlen

import kotlinx.io.Sink
import kotlinx.io.Source

interface VarCodec<T, U> {
    fun serialize(value: T, stream: Sink)

    fun deserialize(stream: Source): T

    fun zigzag(value: T): U
}