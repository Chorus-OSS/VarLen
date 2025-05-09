package org.chorus_oss.varlen.types

import kotlinx.io.Buffer
import org.chorus_oss.varlen.VarCodec
import org.chorus_oss.varlen.VarLen

val VarLen.Int by lazy {
    object : VarCodec<Int, UInt> {
        override fun serialize(value: Int, stream: Buffer) {
            VarLen.UInt.serialize(this.zigzag(value), stream)
        }

        override fun deserialize(stream: Buffer): Int {
            return VarLen.UInt.zigzag(VarLen.UInt.deserialize(stream))
        }

        override fun zigzag(value: Int): UInt {
            return ((value shl 1) xor (value shr 31)).toUInt()
        }
    }
}

fun Buffer.writeIntVar(int: Int) {
    VarLen.Int.serialize(int, this)
}

fun Buffer.readIntVar(): Int {
    return VarLen.Int.deserialize(this)
}