/**
 * <pre>Protocol Primitive Types
 * The protocol is built out of the following primitive types.
 * Fixed Width Primitives
 * int8, int16, int32, int64 - Signed integers with the given precision (in bits) stored in big endian order.
 * Variable Length Primitives
 * bytes, string - These types consist of a signed integer giving a length N followed by N bytes of content. A length of
 * -1 indicates null. string uses an int16 for its size, and bytes uses an int32.
 * Arrays
 * This is a notation for handling repeated structures. These will always be encoded as an int32 size containing the
 * length N followed by N repetitions of the structure which can itself be made up of other primitive types. In the BNF
 * grammars below we will show an array of a structure foo as [foo].
 * </pre>
 */
package me.destinyshine.cicada.broker;