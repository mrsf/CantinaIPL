package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.util.ArrayList;
import java.util.List;

public class Reserva {

	public static final String REFEICAO_ALMOCO = "almoco";
	public static final String REFEICAO_JANTAR = "jantar";

	private String idRefeicao;
	private String refeicao;
	private String dataCompra;
	private String dataUso;
	private String cantina;
	private double precoReserva;
	private String fk_pratoCarnePeixe;
	private String fk_pratoSopa;
	private String fk_pratoSobremesa;
	private List<Dish> pratoList;

	public Reserva() {
	}

	public Reserva(String idRefeicao, List<Dish> pratoList, String refeicao,
			String dataCompra, String dataUso, String cantina,
			double precoReserva) {
		this.pratoList = new ArrayList<Dish>();
		this.pratoList = pratoList;
		this.refeicao = refeicao;
		this.dataCompra = dataCompra;
		this.dataUso = dataUso;
		this.cantina = cantina;
		this.precoReserva = precoReserva;
		this.idRefeicao = idRefeicao;
	}

	public String getIdRefeicao() {
		return idRefeicao;
	}

	public void setIdRefeicao(String idRefeicao) {
		this.idRefeicao = idRefeicao;
	}

	public String getFkPratoPeixeCarne() {
		return fk_pratoCarnePeixe;
	}

	public void setFkPratoCarnePeixe(String fk_pratoCarnePeixe) {
		this.fk_pratoCarnePeixe = fk_pratoCarnePeixe;
	}

	public String getFkPratoSopa() {
		return fk_pratoSopa;
	}

	public void setFkPratoSopa(String fk_pratoSopa) {
		this.fk_pratoSopa = fk_pratoSopa;
	}

	public String getFkPratoSobremesa() {
		return fk_pratoSobremesa;
	}

	public void setFkPratoSobremesa(String fk_pratoSobremesa) {
		this.fk_pratoSobremesa = fk_pratoSobremesa;
	}

	public double getPrecoReserva() {
		return precoReserva;
	}

	public void setPrecoReserva(double precoReserva) {
		this.precoReserva = precoReserva;
	}

	public String getRefeicao() {
		return refeicao;
	}

	public void setRefeicao(String refeicao) {
		this.refeicao = refeicao;
	}

	public String getDataUso() {
		return dataUso;
	}

	public void setDataUso(String dataUso) {
		this.dataUso = dataUso;
	}

	public String getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}

	public String getCantina() {
		return cantina;
	}

	public void setCantina(String cantina) {
		this.cantina = cantina;
	}

	public List<Dish> getPratoList() {
		return pratoList;
	}

	public void setPratoList(ArrayList<Dish> pratoList) {
		this.pratoList = new ArrayList<Dish>();
		this.pratoList = pratoList;
	}

	public Dish getPratoIndex(int index) {
		try {
			return pratoList.get(index);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.getMessage();
		}
		return null;
	}

	public void addPratoToList(int index, Dish prato) {
		this.pratoList.add(index, prato);
	}
}
