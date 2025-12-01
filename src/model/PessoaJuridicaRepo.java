package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaJuridicaRepo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<PessoaJuridica> pessoas = new ArrayList<>();

    public void inserir(PessoaJuridica p) {
        excluir(p.getId());
        pessoas.add(p);
    }

    public void alterar(PessoaJuridica p) {
        excluir(p.getId());
        pessoas.add(p);
    }

    public void excluir(int id) {
        pessoas.removeIf(p -> p.getId() == id);
    }

    public PessoaJuridica obter(int id) {
        for (PessoaJuridica p : pessoas) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public List<PessoaJuridica> obterTodos() {
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
            List<PessoaJuridica> l = (List<PessoaJuridica>) ois.readObject();
            pessoas.clear();
            pessoas.addAll(l);
        }
    }
}

