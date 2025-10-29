package com.example.frota.parametros;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

// Seeder
@Component
@Order(1)                    // (opcional) define a ordem se houver vários seeders
@RequiredArgsConstructor
public class ParametroSeeder implements CommandLineRunner {
  private final ParametroRepository repo;

  @Override
  @Transactional
  public void run(String... args) {
    if (repo.count() == 0) { // 👈 tabela vazia?
      repo.saveAll(List.of(
        new Parametro(null,"FATOR_CUBAGEM","300"),
        new Parametro(null,"CUSTO_PESO","2.5"),
        new Parametro(null,"CUSTO_KM","3")
      ));
    }
  }
}