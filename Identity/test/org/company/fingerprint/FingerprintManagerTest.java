package org.company.fingerprint;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.company.fingerprint.agent.FingerprintData;
import org.company.fingerprint.agent.FingerprintManager;
import org.company.fingerprint.agent.FingerprintProvider;
import org.junit.Test;

import sourceafis.simple.Fingerprint;

public class FingerprintManagerTest {

	@Test
	public void testLoadFingerprints() {
		
		FingerprintManager matcher = new FingerprintManager(new FingerprintProvider());
		try {
			matcher.LoadFingerprints();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Failed with exception "+ e.getMessage());
		}
	}

	@Test
	public void testFindMatches() {
		FingerprintProvider provider =  new FingerprintProvider();
		provider.Open();
		try {
			
			List<FingerprintData> fps = provider.GetFingerprints();
		
			FingerprintManager matcher = new FingerprintManager(provider);
			matcher.LoadFingerprints();
			
			
			List<FingerprintData> matches =  matcher.FindMatches(fps.get(0), false);
			
			if(matches == null || matches.isEmpty())
			{
				fail("No match found !");
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Failed with exception "+ e.getMessage());
		}
		
	}

	
}
