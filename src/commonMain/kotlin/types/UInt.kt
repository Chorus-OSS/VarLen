package org.chorus_oss.varlen.types

import kotlinx.io.*
import org.chorus_oss.varlen.VarCodec
import org.chorus_oss.varlen.VarLen

val VarLen.UInt by lazy {
    object : VarCodec<UInt, Int> {
        override fun serialize(value: UInt, stream: Buffer) {
            var mValue = value

            while (mValue >= 128u) {
                val next = ((mValue and 127u) or 128u).toByte()
                mValue = mValue shr 7

                stream.writeByte(next)
            }

            stream.writeByte((mValue and 127u).toByte())
        }

        override fun deserialize(stream: Buffer): UInt {
            var shift = 0
            var decoded = 0u
            var next: Byte

            while (true) {
                next = stream.readByte()
                decoded = decoded or ((next.toUInt() and 127u) shl shift)

                if (next.toInt() and 128 != 0) {
                    shift += 7
                }
                else return decoded
            }
        }

        override fun zigzag(value: UInt): Int {
            return (value.toInt() shr 1) xor -(value.toInt() and 1)
        }
    }
}

fun Buffer.writeUIntVar(int: UInt) {
    VarLen.UInt.serialize(int, this)
}

fun Buffer.readUIntVar(): UInt {
    return VarLen.UInt.deserialize(this)
}