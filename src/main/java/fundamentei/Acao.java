package fundamentei;

public class Acao implements Comparable<Acao> {

	private String nome;
	private String codigo;
	private Double nota;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	@Override
	public String toString() {
		return nome + " " + codigo + " " + nota;
	}

	public String toCSV() {
		return nome + ";" + codigo + ";" + nota;
	}

	public int compareTo(Acao o) {
		return nota.compareTo(o.nota);
	}
}
