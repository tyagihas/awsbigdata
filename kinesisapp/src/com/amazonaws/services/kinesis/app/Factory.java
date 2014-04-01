package com.amazonaws.services.kinesis.app;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;

public class Factory implements IRecordProcessorFactory {

	@Override
	public IRecordProcessor createProcessor() {
		return new Processor();
	}

}
