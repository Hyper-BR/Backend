package br.com.hyper.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.UUID;

@Data
@Setter
@Getter
@AllArgsConstructor
public class ListenerDTO {
    private UUID id;
    private String name;
    private BigInteger plays;
}
