/**
 * 
 */
package net.sf.openrocket;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.simulation.FlightDataBranch;
import net.sf.openrocket.startup.OpenRocketAPI;
import net.sf.openrocket.simulation.*;

/**
 * @author nubjub
 */
public class RunVsStep extends OpenRocketAPI{
	SimulationStatus rc_simStatus;
	FlightDataBranch rc_flightData;
	ArrayList<FlightDataBranch> rc_fdl;
	double [][] sensor_matrix = {{1,0,0},{0,1,0},{0,0,1}};

	public RunVsStep(){
		super();
	}
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	    this.SetRandomSeed(1);
		try {
		    System.out.println("Opening file");
		    this.LoadRocket("/home/panman/desk/src/openrocket/resources-psas/threeStageRocket.ork");
		}
		catch (Exception e){
			System.out.println("Failure to open file");
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStep() {
		this.StartSimulation();
		while(this.IsSimulationLoopRunning()){
			while(this.IsSimulationLoopRunning()){
				int iteration = this.SimulationStep();
				if(this.IsSimulationLoopRunning()){
					int rval = GetData("/home/panman/desk/src/openrocket/resources-psas/step.csv", null, -1);
				}
			}
			this.StagesStep();
		}
		this.FlightDataStepToCSV("close");
	}
	
	@Test
	public void testRun() {
		this.RunSimulation();
		FlightData fd = m_CFlightData;
		int branches = m_CFlightData.getBranchCount();
		FlightDataBranch fdb = null;
		for(int j =0; j < branches; j++){
			fdb = fd.getBranch(j);
			int fdb_length = fdb.getLength()+1; //(iterations start at 1)
			for(int i =1; i < fdb_length; i++){
				int rval = GetData("/home/panman/desk/src/openrocket/resources-psas/run.csv", fdb, i);
			}
		}
		this.FlightDataStepToCSV("close");
	}

	private int GetData(String CSVFile, FlightDataBranch b, int i){
		if(i < 0){
			return this.FlightDataStepToCSV(CSVFile);
		}
		else{
			return this.FlightDataStepToCSV(CSVFile, b, i);
		}
	}
}
