package tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDGT2821Generator {
    private static String escapePdf(String s) {
        return s.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private static byte[] buildPdf(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        List<Integer> offsets = new ArrayList<>();

        sb.append("%PDF-1.4\n");
        // 1: Catalog
        offsets.add(sb.length());
        sb.append("1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj\n");
        // 2: Pages
        offsets.add(sb.length());
        sb.append("2 0 obj << /Type /Pages /Count 1 /Kids [3 0 R] >> endobj\n");
        // 3: Page
        offsets.add(sb.length());
        sb.append("3 0 obj << /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >> endobj\n");
        // 4: Font
        offsets.add(sb.length());
        sb.append("4 0 obj << /Type /Font /Subtype /Type1 /BaseFont /Helvetica >> endobj\n");
        // 5: Contents
        StringBuilder content = new StringBuilder();
        content.append("BT\n");
        // Title
        content.append("/F1 18 Tf\n20 TL\n50 800 Td\n");
        content.append("(").append(escapePdf("Universidade Estacio do Gama")).append(") Tj\n");
        content.append("T*\n");
        content.append("(").append(escapePdf("Relatorio de Pratica - DGT2821 Programacao Back-end com Java")).append(") Tj\n");
        content.append("T*\n");
        // Body
        content.append("/F1 12 Tf\n16 TL\n");
        for (int i = 0; i < lines.size(); i++) {
            String ln = escapePdf(lines.get(i));
            content.append("(").append(ln).append(") Tj\n");
            if (i < lines.size() - 1) content.append("T*\n");
        }
        content.append("ET\n");
        byte[] contentBytes = content.toString().getBytes(StandardCharsets.UTF_8);
        offsets.add(sb.length());
        sb.append("5 0 obj << /Length ").append(contentBytes.length).append(" >>\nstream\n");
        sb.append(content.toString());
        sb.append("endstream\nendobj\n");

        // xref
        int xrefPos = sb.length();
        sb.append("xref\n");
        sb.append("0 6\n");
        sb.append(String.format("%010d 65535 f \n", 0));
        for (int off : offsets) {
            sb.append(String.format("%010d 00000 n \n", off));
        }
        sb.append("trailer << /Size 6 /Root 1 0 R >>\n");
        sb.append("startxref\n").append(xrefPos).append("\n%%EOF\n");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("Campus: Estacio do Gama");
        lines.add("Curso: Back-end com Java (DGT2821)");
        lines.add("Disciplina: Programacao Back-end com Java");
        lines.add("Projeto: CadastroPOO");
        lines.add("Repositorio: https://github.com/alexanderjr02/cadastro-de-clientes");
        lines.add("");
        lines.add("Objetivo da pratica:");
        lines.add("- Implementar cadastro de clientes em modo texto");
        lines.add("- Utilizar POO (heranca e polimorfismo)");
        lines.add("- Persistencia de objetos em arquivos binarios");
        lines.add("");
        lines.add("Codigos principais:");
        lines.add("- Pessoa, PessoaFisica, PessoaJuridica");
        lines.add("- Repositorios (persistir/recuperar)");
        lines.add("- CLI (CadastroPOO) com menu completo");
        lines.add("");
        lines.add("Resultados:");
        lines.add("- Menu funcional; incluir/alterar/excluir/obter/listar");
        lines.add("- Salvar e recuperar com tratamento de excecoes");
        lines.add("");
        lines.add("Analise e conclusao:");
        lines.add("- Heranca: reaproveitamento vs acoplamento");
        lines.add("- Serializable: necessario para ObjectOutputStream/ObjectInputStream");
        lines.add("- Streams: paradigma funcional (forEach/map/filter) em listas");
        lines.add("- Padrao de arquivos: repositorio + serializacao de objetos");
        lines.add("- Main estatico: invocacao sem instancia; Scanner para entrada");

        byte[] pdf = buildPdf(lines);
        File outDir = new File("docs");
        if (!outDir.exists()) outDir.mkdirs();
        try (FileOutputStream fos = new FileOutputStream(new File(outDir, "Relatorio-Pratica-DGT2821.pdf"))) {
            fos.write(pdf);
        }
        System.out.println("Relatório gerado em docs/Relatorio-Pratica-DGT2821.pdf");
    }
}
