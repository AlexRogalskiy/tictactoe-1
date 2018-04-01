package ris58h.tictactoe.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ris58h.tictactoe.util.ByteArrayDeserializer;
import ris58h.tictactoe.util.ByteArraySerializer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Game {
    public static byte EMPTY = 0;
    public static byte X = 1;
    public static byte O = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @JsonSerialize(using = ByteArraySerializer.class)
    @JsonDeserialize(using = ByteArrayDeserializer.class)
    public byte[] board;

    public boolean finished = false;
}