package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import modelo.DAOReporte;
import vista.inicioVISTA;
import vista.reporteVISTA;

/**
 *
 * @author JE
 */
public class ReporteControlador {
    private reporteVISTA view;
    private DAOReporte daoReporte;

    public ReporteControlador(reporteVISTA view) {
        this.view = view;
        this.daoReporte = new DAOReporte();
        listarReporte();
        
        this.view.getBtnATRAS().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresarInicio();
            }
        });
    }
    

    public void listarReporte() {
        DefaultTableModel model = (DefaultTableModel) view.getTablaReporte().getModel();
        model.setRowCount(0); 

        List<Object[]> reporte = daoReporte.listarReporte();
        for (Object[] row : reporte) {
            model.addRow(row);
        }
    }
    
    private void regresarInicio() {
        this.view.setVisible(false);
        inicioVISTA inicioVista = new inicioVISTA();
        InicioControlador inicioControlador = new InicioControlador(inicioVista);
        inicioVista.setVisible(true);
    }
}
