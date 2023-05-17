UPDATE PROCESSDEFINITION
SET DEFINITION = 
	SUBSTR(DEFINITION, 1, INSTR(DEFINITION, ' id="') + LENGTH(' id="') - 1) 
	|| 
	PROCESSID
	|| 
	--Get the remaining part of the string, starting with the '"' character after the processID
	SUBSTR(
			--Get the string starting with the first character of the processID excluding the '"' character
		SUBSTR(DEFINITION, INSTR(DEFINITION, ' id="') + LENGTH(' id="')), 
		--Get the first occurrance of the '"' character after the processID
		INSTR(SUBSTR(DEFINITION, INSTR(DEFINITION, ' id="') + LENGTH(' id="')), '"')
	);