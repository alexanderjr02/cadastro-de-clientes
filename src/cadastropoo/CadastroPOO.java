package cadastropoo;

import model.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CadastroPOO {
    private static final Scanner scanner = new Scanner(System.in);

    private static final PessoaFisicaRepo repoFisica = new PessoaFisicaRepo();
    private static final PessoaJuridicaRepo repoJuridica = new PessoaJuridicaRepo();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Cadastro de Clientes ===");
            System.out.println("1 - Incluir");
            System.out.println("2 - Alterar");
            System.out.println("3 - Excluir");
            System.out.println("4 - Exibir pelo ID");
            System.out.println("5 - Exibir todos");
            System.out.println("6 - Salvar dados");
            System.out.println("7 - Recuperar dados");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            String opStr = scanner.nextLine().trim();
            int opcao = parseInt(opStr, -1);

            switch (opcao) {
                case 1 -> incluir();
                case 2 -> alterar();
                case 3 -> excluir();
                case 4 -> exibirPorId();
                case 5 -> exibirTodos();
                case 6 -> salvar();
                case 7 -> recuperar();
                case 0 -> {
                    System.out.println("Encerrando...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void incluir() {
        char tipo = lerTipo();
        System.out.print("ID: ");
        int id = parseInt(scanner.nextLine(), 0);
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        if (tipo == 'F') {
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Idade: ");
            int idade = parseInt(scanner.nextLine(), 0);
            repoFisica.inserir(new PessoaFisica(id, nome, cpf, idade));
        } else {
            System.out.print("CNPJ: ");
            String cnpj = scanner.nextLine();
            repoJuridica.inserir(new PessoaJuridica(id, nome, cnpj));
        }
        System.out.println("Incluído com sucesso.");
    }

    private static void alterar() {
        char tipo = lerTipo();
        System.out.print("ID: ");
        int id = parseInt(scanner.nextLine(), 0);
        if (tipo == 'F') {
            PessoaFisica atual = repoFisica.obter(id);
            if (atual == null) { System.out.println("Não encontrado."); return; }
            System.out.println("Atual: " + atual.exibir());
            System.out.print("Novo nome: ");
            String nome = scanner.nextLine();
            System.out.print("Novo CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Nova idade: ");
            int idade = parseInt(scanner.nextLine(), atual.getIdade());
            repoFisica.alterar(new PessoaFisica(id, nome, cpf, idade));
        } else {
            PessoaJuridica atual = repoJuridica.obter(id);
            if (atual == null) { System.out.println("Não encontrado."); return; }
            System.out.println("Atual: " + atual.exibir());
            System.out.print("Novo nome: ");
            String nome = scanner.nextLine();
            System.out.print("Novo CNPJ: ");
            String cnpj = scanner.nextLine();
            repoJuridica.alterar(new PessoaJuridica(id, nome, cnpj));
        }
        System.out.println("Alterado com sucesso.");
    }

    private static void excluir() {
        char tipo = lerTipo();
        System.out.print("ID: ");
        int id = parseInt(scanner.nextLine(), 0);
        if (tipo == 'F') repoFisica.excluir(id); else repoJuridica.excluir(id);
        System.out.println("Excluído com sucesso.");
    }

    private static void exibirPorId() {
        char tipo = lerTipo();
        System.out.print("ID: ");
        int id = parseInt(scanner.nextLine(), 0);
        if (tipo == 'F') {
            PessoaFisica p = repoFisica.obter(id);
            System.out.println(p == null ? "Não encontrado." : p.exibir());
        } else {
            PessoaJuridica p = repoJuridica.obter(id);
            System.out.println(p == null ? "Não encontrado." : p.exibir());
        }
    }

    private static void exibirTodos() {
        char tipo = lerTipo();
        if (tipo == 'F') {
            List<PessoaFisica> l = repoFisica.obterTodos();
            if (l.isEmpty()) System.out.println("Vazio.");
            else l.forEach(p -> System.out.println(p.exibir()));
        } else {
            List<PessoaJuridica> l = repoJuridica.obterTodos();
            if (l.isEmpty()) System.out.println("Vazio.");
            else l.forEach(p -> System.out.println(p.exibir()));
        }
    }

    private static void salvar() {
        System.out.print("Prefixo dos arquivos: ");
        String prefixo = scanner.nextLine().trim();
        try {
            repoFisica.persistir(prefixo + ".fisica.bin");
            repoJuridica.persistir(prefixo + ".juridica.bin");
            System.out.println("Dados salvos.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    private static void recuperar() {
        System.out.print("Prefixo dos arquivos: ");
        String prefixo = scanner.nextLine().trim();
        try {
            repoFisica.recuperar(prefixo + ".fisica.bin");
            repoJuridica.recuperar(prefixo + ".juridica.bin");
            System.out.println("Dados recuperados.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao recuperar: " + e.getMessage());
        }
    }

    private static char lerTipo() {
        while (true) {
            System.out.print("Tipo (F/J): ");
            String s = scanner.nextLine().trim().toUpperCase();
            if (s.equals("F") || s.equals("J")) return s.charAt(0);
            System.out.println("Tipo inválido. Digite 'F' ou 'J'.");
        }
    }

    private static int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}

