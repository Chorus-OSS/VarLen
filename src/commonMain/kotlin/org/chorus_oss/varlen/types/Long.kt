package org.chorus_oss.varlen.types

import kotlinx.io.Sink
import kotlinx.io.Source
import org.chorus_oss.varlen.VarCodec
import org.chorus_oss.varlen.VarLen

val VarLen.Long by lazy {
    object : VarCodec<Long, ULong> {
        override fun serialize(value: Long, stream: Sink) {
            VarLen.ULong.serialize(this.zigzag(value), stream)
        }

        override fun deserialize(stream: Source): Long {
            return VarLen.ULong.zigzag(VarLen.ULong.deserialize(stream))
        }

        override fun zigzag(value: Long): ULong {
            return ((value shl 1) xor (value shr 63)).toULong()
        }
    }
}

fun Sink.writeLongVar(long: Long) {
    VarLen.Long.serialize(long, this)
}

fun Source.readLongVar(): Long {
    return VarLen.Long.deserialize(this)
}