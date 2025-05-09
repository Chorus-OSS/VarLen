package org.chorus_oss.varlen.types

import kotlinx.io.*
import org.chorus_oss.varlen.VarCodec
import org.chorus_oss.varlen.VarLen

val VarLen.ULong by lazy {
    object : VarCodec<ULong, Long> {
        override fun serialize(value: ULong, stream: Buffer) {
            var mValue = value

            while (mValue >= 128u) {
                val next = ((mValue and 127u) or 128u).toByte()
                mValue = mValue shr 7

                stream.writeByte(next)
            }

            stream.writeByte((mValue and 127u).toByte())
        }

        override fun deserialize(stream: Buffer): ULong {
            var shift = 0
            var decoded: ULong = 0u
            var next: Byte

            while (true) {
                next = stream.readByte()
                decoded = decoded or ((next.toULong() and 127u) shl shift)

                if (next.toInt() and 128 != 0) {
                    shift += 7
                }
                else return decoded
            }
        }

        override fun zigzag(value: ULong): Long {
            return (value.toLong() shr 1) xor -(value.toLong() and 1)
        }
    }
}