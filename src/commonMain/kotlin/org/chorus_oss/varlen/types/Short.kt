package org.chorus_oss.varlen.types

import kotlinx.io.Sink
import kotlinx.io.Source
import org.chorus_oss.varlen.VarCodec
import org.chorus_oss.varlen.VarLen

val VarLen.Short by lazy {
    object : VarCodec<Short, UShort> {
        override fun serialize(value: Short, stream: Sink) {
            VarLen.UShort.serialize(this.zigzag(value), stream)
        }

        override fun deserialize(stream: Source): Short {
            return VarLen.UShort.zigzag(VarLen.UShort.deserialize(stream))
        }

        override fun zigzag(value: Short): UShort {
            return ((value.toInt() shl 1) xor (value.toInt() shr 15)).toUShort()
        }
    }
}

fun Sink.writeShortVar(short: Short) {
    VarLen.Short.serialize(short, this)
}

fun Source.readShortVar(): Short {
    return VarLen.Short.deserialize(this)
}