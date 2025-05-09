package org.chorus_oss.varlen.types

import kotlinx.io.Buffer
import org.chorus_oss.varlen.VarCodec
import org.chorus_oss.varlen.VarLen

val VarLen.Long by lazy {
    object : VarCodec<Long, ULong> {
        override fun serialize(value: Long, stream: Buffer) {
            VarLen.ULong.serialize(this.zigzag(value), stream)
        }

        override fun deserialize(stream: Buffer): Long {
            return VarLen.ULong.zigzag(VarLen.ULong.deserialize(stream))
        }

        override fun zigzag(value: Long): ULong {
            return ((value shl 1) xor (value shr 63)).toULong()
        }
    }
}