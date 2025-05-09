package org.chorus_oss.varlen

import kotlinx.io.Buffer

interface VarCodec<T, U> {
    fun serialize(value: T, stream: Buffer)

    fun deserialize(stream: Buffer): T

    fun zigzag(value: T): U
}