import java.io.ByteArrayOutputStream
import java.io.File

fun main() {
    val input = File("input/day16.txt").readText()
    val buf = decodeHex(input)
    val packet = readPacket(BitReader(buf))
    println(versionSum(packet))
    println(eval(packet))
}

fun ByteArray.bitAt(index: Int): Boolean {
    val byte = get(index / Byte.SIZE_BITS).toInt()
    val mask = 1 shl (Byte.SIZE_BITS - index % Byte.SIZE_BITS - 1)
    return (byte and mask) != 0
}

val CHARS = "0123456789ABCDEF"

fun decodeHex(s: String): ByteArray {
    val out = ByteArrayOutputStream()
    for (pair in s.chunked(2)) {
        val high = CHARS.indexOf(pair[0])
        val low = if (pair.length >= 2) CHARS.indexOf(pair[1]) else 0
        out.write((high shl 4) or low)
    }
    return out.toByteArray()
}

class BitReader(private val buf: ByteArray) {
    private var position = 0

    fun position() = position

    fun read(numOfBits: Int): Int {
        val nextPosition = position + numOfBits
        val result: Int = (position until nextPosition).fold(0) { acc, i ->
            (acc shl 1) or (if (buf.bitAt(i)) 1 else 0)
        }
        position = nextPosition
        return result
    }
}

sealed class Packet(
    val version: Int,
    val typeID: Int,
) {
    class Literal(
        version: Int,
        typeID: Int,
        val value: Long,
    ) : Packet(version, typeID) {
        override fun toString(): String {
            return "Literal(version=$version, typeID=$typeID, value=$value)"
        }
    }

    class Operator(
        version: Int,
        typeID: Int,
        val children: List<Packet>,
    ) : Packet(version, typeID) {
        override fun toString(): String {
            return "Operator(version=$version, typeID=$typeID, children=$children)"
        }
    }
}

fun readPacket(reader: BitReader): Packet {
    // header: version(3bit), type ID(3bit)
    val version = reader.read(3)
    val typeID = reader.read(3)

    return if (typeID == 4) {
        // type ID = 4 : literal value
        //   5bit groups (first 1bit = 1 except last group)
        var value = 0L
        while (true) {
            val stop = reader.read(1) == 0
            val group = reader.read(4)
            value = (value shl 4) or group.toLong()
            if (stop) break
        }
        Packet.Literal(version, typeID, value)
    } else {
        // type ID != 4 : operator
        //   length type ID (1bit)
        //     if 0 -> length(15 bit) + data(variable)
        //     if 1 -> count(11 bit) + sub-packets
        val lengthTypeID = reader.read(1)
        if (lengthTypeID == 0) {
            var length = reader.read(15)
            val children = mutableListOf<Packet>()
            while (true) {
                val p0 = reader.position()
                children.add(readPacket(reader))
                length -= reader.position() - p0
                if (length <= 0) break
            }
            Packet.Operator(version, typeID, children)
        } else {
            val subPacketCount = reader.read(11)
            Packet.Operator(version, typeID, List(subPacketCount) { readPacket(reader) })
        }
    }
}

fun versionSum(packet: Packet): Int {
    return packet.version + when (packet) {
        is Packet.Operator ->
            packet.children.sumOf { versionSum(it) }
        else ->
            0
    }
}

fun eval(packet: Packet): Long {
    return when (packet) {
        is Packet.Literal -> packet.value
        is Packet.Operator -> when (packet.typeID) {
            0 -> {
                // sum
                packet.children.sumOf { eval(it) }
            }
            1 -> {
                // product
                packet.children.fold(1) { acc, child -> acc * eval(child) }
            }
            2 -> {
                // minimum
                packet.children.minOf { eval(it) }
            }
            3 -> {
                // maximum
                packet.children.maxOf { eval(it) }
            }
            5 -> {
                // greater than
                if (eval(packet.children[0]) > eval(packet.children[1])) 1 else 0
            }
            6 -> {
                // less than
                if (eval(packet.children[0]) < eval(packet.children[1])) 1 else 0
            }
            7 -> {
                // equal to
                if (eval(packet.children[0]) == eval(packet.children[1])) 1 else 0
            }
            else -> error("unknown typeID: ${packet.typeID}")
        }
    }
}
