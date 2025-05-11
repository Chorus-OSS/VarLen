package org.chorus_oss.varlen.types

import kotlinx.io.*
import org.chorus_oss.varlen.VarCodec
import org.chorus_oss.varlen.VarLen

val VarLen.UShort by lazy {
    object : VarCodec<UShort, Short> {
        override fun serialize(value: UShort, stream: Sink) {
            var mValue = value.toUInt()

            while (mValue >= 128u) {
                val next = ((mValue and 127u) or 128u).toByte()
                mValue = mValue shr 7

                stream.writeByte(next)
            }

            stream.writeByte((mValue and 127u).toByte())
        }

        override fun deserialize(stream: Source): UShort {
            var shift = 0
            var decoded = 0u
            var next: Byte

            while (true) {
                next = stream.readByte()
                decoded = decoded or ((next.toUInt() and 127u) shl shift)

                if (next.toInt() and 128 != 0) {
                    shift += 7
                }
                else return decoded.toUShort()
            }
        }

        override fun zigzag(value: UShort): Short {
            return ((value.toInt() shr 1) xor -(value.toInt() and 1)).toShort()
        }
    }
}

fun Sink.writeUShortVar(short: UShort) {
    VarLen.UShort.serialize(short, this)
}

fun Source.readUShortVar(): UShort {
    return VarLen.UShort.deserialize(this)
}