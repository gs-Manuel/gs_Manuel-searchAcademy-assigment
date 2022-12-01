package com.github.gsManuel.APIWEB.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Principal {
    String name;
    String characters;
}
