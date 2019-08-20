/*
* EXEMPLO DE IMPLEMENTAÇÃO UTILIZANDO A BIBLIOTECA OFX4J.
* REFERENCIA DE CÓDIGO
* https://comunidadecc.blogspot.com/2010/08/lendo-arquivo-ofx-com-ofx4j.html
* */
package br.com.estudo;

import br.com.estudo.objetos.Banco;
import br.com.estudo.objetos.Transacao;
import net.sf.ofx4j.domain.data.MessageSetType;
import net.sf.ofx4j.domain.data.ResponseEnvelope;
import net.sf.ofx4j.domain.data.ResponseMessageSet;
import net.sf.ofx4j.domain.data.banking.BankingResponseMessageSet;
import net.sf.ofx4j.domain.data.signon.SignonResponse;
import net.sf.ofx4j.io.AggregateUnmarshaller;
import net.sf.ofx4j.io.OFXParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Principal {

    public static void main(String[] args) {
        try {
            AggregateUnmarshaller unmarshaller = new AggregateUnmarshaller(ResponseEnvelope.class);
            ResponseEnvelope re = (ResponseEnvelope) unmarshaller.unmarshal(new FileInputStream(new File(System.getProperty("user.home") + "/Desktop/teste.ofx")));

            // OBJETO CONTENDO OS INFORMAÇÕES COMO INSTITUIÇÃO FINANCEIRA, IDIOMA, DATA DA CONTA
            SignonResponse sr = re.getSignonResponse();

            // CAPTURAR LISTA DE TRANSAÇÕES
            MessageSetType type = MessageSetType.banking;
            ResponseMessageSet message = re.getMessageSet(type);

            // DECLARANDO A LISTA DO NOSSO OBJETO
            List<Banco> meusBancos = new ArrayList<>();
            if (Objects.nonNull(message)) {
                // POPULANDO OBJETOS PRÓPRIOS
                popularObjetos(((BankingResponseMessageSet) message), meusBancos);
                // IMPRIMINDO VALORES
                imprimirArquivo(meusBancos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OFXParseException e) {
            e.printStackTrace();
        }
    }

    private static void popularObjetos(BankingResponseMessageSet bank, List<Banco> bancos) {
        bank.getStatementResponses().stream().forEach(b -> {
            Banco banco = new Banco();
            banco.setNomeBanco(b.getMessage().getAccount().getBankId());
            banco.setAgencia(b.getMessage().getAccount().getBranchId());
            banco.setConta(b.getMessage().getAccount().getAccountNumber());
            banco.setSaldo(BigDecimal.valueOf(b.getMessage().getLedgerBalance().getAmount()));
            banco.setDataArquivo(b.getMessage().getLedgerBalance().getAsOfDate());
            b.getMessage().getTransactionList().getTransactions().stream().forEach(t -> {
                Transacao transacao = new Transacao();
                transacao.setNomeTipoTransacao(t.getTransactionType().name());
                transacao.setIdTransacao(t.getId());
                transacao.setDataTransacao(t.getDatePosted());
                transacao.setValor(BigDecimal.valueOf(t.getAmount()));
                transacao.setDescricao(t.getMemo());
                banco.getTransacaoList().add(transacao);
            });
            bancos.add(banco);
        });
    }

    private static void imprimirArquivo(List<Banco> meusBancos) {
        for (Banco banco : meusBancos) {
            System.out.println("banco: " + banco.getNomeBanco());
            System.out.println("ag: " + banco.getAgencia());
            System.out.println("cc: " + banco.getConta());
            System.out.println("balanço final: " + banco.getSaldo());
            System.out.println("data do arquivo: " + formataData(banco.getDataArquivo()));
            for (Transacao transacao : banco.getTransacaoList()) {
                System.out.println("tipo: " + transacao.getNomeTipoTransacao());
                System.out.println("id: " + transacao.getIdTransacao());
                System.out.println("data: " + formataData(transacao.getDataTransacao()));
                System.out.println("valor: " + transacao.getValor());
                System.out.println("descrição: " + transacao.getDescricao());
                System.out.println("");
            }
        }
    }

    private static Date formataData(Date data) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return f.parse(f.format(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
