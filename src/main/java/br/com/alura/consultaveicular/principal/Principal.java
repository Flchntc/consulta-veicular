package br.com.alura.consultaveicular.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.consultaveicular.models.Dados;
import br.com.alura.consultaveicular.models.DadosVeiculo;
import br.com.alura.consultaveicular.models.Modelos;
import br.com.alura.consultaveicular.services.ConsumoApi;
import br.com.alura.consultaveicular.services.ConverteDados;

public class Principal {
    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    public void exibeMenu() {
        var menu = """
                *** OPÇÕES ***
                Carro
                Moto
                Caminhão

                Digite uma das opções para consultar:
                """;

        System.out.println(menu);
        var opcao = leitura.nextLine();

        String endereco = null;

        if(opcao.toLowerCase().contains("carr")){
            endereco = URL_BASE + "carros/marcas";
        } else if(opcao.toLowerCase().contains("mot")){
            endereco = URL_BASE + "motos/marcas";
        } else if(opcao.toLowerCase().contains("camin")){
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumo.obterDados(endereco);

        var marcas = conversor.obterLista(json,Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
        
        System.out.println("Informe o codigo da marca para consulta: ");
        var codigoMarca = leitura.nextLine();
        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);
        
        System.out.println("\n Modelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\n Digite um trecho do modelo de carro que deseja buscar:");
        var modeloCarro = leitura.nextLine();
        
        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(modeloCarro.toLowerCase()))
                        .collect(Collectors.toList());

        System.out.println("\nModelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite por favor o codigo do modelo escolhido para buscar os valores de avaliação:");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<DadosVeiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            DadosVeiculo veiculo = conversor.obterDados (json, DadosVeiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veiculos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);
    }
}
