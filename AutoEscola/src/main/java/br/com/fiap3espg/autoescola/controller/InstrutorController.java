package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.instrutor.InstrutorDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instrutores")
public class InstrutorController {
    @PostMapping
    public void cadastrarInstrutor(@RequestBody InstrutorDTO dados) {
        System.out.println(dados);
    }
}
