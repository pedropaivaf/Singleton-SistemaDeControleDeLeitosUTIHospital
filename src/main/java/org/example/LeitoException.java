package org.example;

/**
 * Exceção para operações inválidas envolvendo leitos de UTI.
 *
 * <p>Lançada quando se tenta, por exemplo, alocar um leito já ocupado
 * ou liberar um leito que já está livre.</p>
 *
 * @author Pedro
 */
public class LeitoException extends RuntimeException {

    /**
     * Cria uma nova exceção com a mensagem informada.
     *
     * @param mensagem descrição do erro ocorrido
     */
    public LeitoException(String mensagem) {
        super(mensagem);
    }
}
