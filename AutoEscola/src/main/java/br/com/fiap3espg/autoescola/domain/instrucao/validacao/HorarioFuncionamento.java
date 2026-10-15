package br.com.fiap3espg.autoescola.domain.instrucao.validacao;

/**
 * Janela de funcionamento da auto escola (segunda a sábado).
 * Instruções duram 1 hora; a última pode começar às 20:00 e terminar às 21:00.
 */
public final class HorarioFuncionamento {
    public static final int HORA_ABERTURA = 6;
    public static final int HORA_FECHAMENTO = 21;
    public static final int ULTIMA_HORA_INICIO = HORA_FECHAMENTO - 1;

    private HorarioFuncionamento() {
    }
}
