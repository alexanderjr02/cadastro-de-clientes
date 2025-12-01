package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaFisicaRepo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<PessoaFisica> pessoas = new ArrayList<>();

    public void inserir(PessoaFisica p) {
        excluir(p.getId());
        pessoas.add(p);
    }

    public void alterar(PessoaFisica p) {
        excluir(p.getId());
        pessoas.add(p);
    }

    public void excluir(int id) {
        pessoas.removeIf(p -> p.getId() == id);
    }

    public PessoaFisica obter(int id) {
        for (PessoaFisica p : pessoas) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public List<PessoaFisica> obterTodos() {
        return new ArrayList<>(pessoas);
    }

    public void persistir(String nomeArquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(pessoas);
        }
    }

    @SuppressWarnings("unchecked")
    public void recuperar(String nomeArquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            List<PessoaFisica> l = (List<PessoaFisica>) ois.readObject();
            pessoas.clear();
            pessoas.addAll(l);
        }
    }
}

