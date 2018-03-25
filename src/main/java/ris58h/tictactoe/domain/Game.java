package ris58h.tictactoe.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Game {
    public static byte EMPTY = 0;
    public static byte X = 1;
    public static byte O = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    byte[] board;

    boolean finished = false;
}