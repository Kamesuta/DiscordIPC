/*
 * Copyright 2017 John Grosh (john.a.grosh@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.discordipc.entities;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.json.JSONObject;

/**
 * A data-packet received from Discord via an {@link com.jagrosh.discordipc.IPCClient IPCClient}.<br>
 * These can be handled via an implementation of {@link com.jagrosh.discordipc.IPCListener IPCListener}.
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class Packet
{
    private final OpCode op;
    private final JSONObject data;
    private final String encoding;

    /**
     * Constructs a new Packet using an {@link OpCode} and {@link JSONObject}.
     *
     * @param op The OpCode value of this new Packet.
     * @param data The JSONObject payload of this new Packet.
     * @param encoding encode
     */
    public Packet(OpCode op, JSONObject data, String encoding)
    {
        this.op = op;
        this.data = data;
        this.encoding = encoding;
    }

    /**
     * Constructs a new Packet using an {@link OpCode} and {@link JSONObject}.
     *
     * @param op The OpCode value of this new Packet.
     * @param data The JSONObject payload of this new Packet.
     */
    @Deprecated
    public Packet(OpCode op, JSONObject data)
    {
        this(op, data, "UTF-8");
    }

    /**
     * Converts this {@link Packet} to a {@code byte} array.
     *
     * @return This Packet as a {@code byte} array.
     */
    public byte[] toBytes()
    {
    	String s = data.toString();
        byte[] d;
        try {
            d = s.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            d = s.getBytes();
        }
        ByteBuffer packet = ByteBuffer.allocate(d.length + 2*Integer.BYTES);
        packet.putInt(Integer.reverseBytes(op.ordinal()));
        packet.putInt(Integer.reverseBytes(d.length));
        packet.put(d);
        return packet.array();
    }

    /**
     * Gets the {@link OpCode} value of this {@link Packet}.
     *
     * @return This Packet's OpCode.
     */
    public OpCode getOp()
    {
        return op;
    }

    /**
     * Gets the {@link JSONObject} value as a part of this {@link Packet}.
     *
     * @return The JSONObject value of this Packet.
     */
    public JSONObject getJson()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return "Pkt:"+getOp()+getJson().toString();
    }

    /**
     * Discord response OpCode values that are
     * sent with response data to and from Discord
     * and the {@link com.jagrosh.discordipc.IPCClient IPCClient}
     * connected.
     */
    public enum OpCode
    {
        HANDSHAKE, FRAME, CLOSE, PING, PONG
    }
}
