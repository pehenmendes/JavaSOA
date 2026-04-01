package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.aluno.AlunoDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    @PostMapping
    public void cadastrarAluno(@RequestBody AlunoDTO dados){
        System.out.println(dados);
    }
}
