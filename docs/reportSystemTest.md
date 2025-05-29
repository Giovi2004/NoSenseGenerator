# System Test Report

## ST1: Sentence Analysis
### Acceptance Criteria:
- **Given** I have not selected the request syntactic tree option
**When** I input a sentence and click the Analyze Sentence button
**Then** the system processes the sentence and displays the Nouns, Verbs and Adjectives from the sentence  
    - **PASSED**

- **Given** I have selected the request syntactic tree option
**When** I input a sentence and click the Analyze Sentence button
**Then** the system processes the sentence, displays the Nouns, Verbs and Adjectives from the sentence and shows the syntactic tree visualization of the input sentence  
    - **PASSED**

- **Given** I have not inputed any sentence
**When** I click the Analyze Sentence button
**Then** the system displays a message requiring to input a sentence    
    - **PASSED**

## ST2: Template Generation
### Acceptance Criteria:
- **Given** I have already analyzed a sentence
**When** I click the Generate Template button
**Then** the system generates and displays a random sentence template with placeholders for nouns, verbs, adjectives and ohter sentences
    - **PASSED**

- **Given** I have not analyzed a sentence
**Then** the Generate Template button is disabled
    - **PASSED**

## ST3: Template Completion
### Acceptance Criteria:
- **Given** I have already generated a sentence template
**When** I click the Generate Sentence button
**Then** the system fills the template with terms from its built-in dictionary and the input sentence and displays the completed sentence
    - **PASSED**

- **Given** I have not generated a sentence template
**Then** the Generate Sentence button is disabled
    - **PASSED**

## ST4: Toxicity Validation
### Acceptance Criteria:
- **Given** I have already generated a sentence
**When** I click the Validate Toxicity button
**Then** the system analyzes the sentence for toxicity and displays the results
    - **PASSED**

- **Given** I have not generated a sentence
**Then** the Validate Toxicity button is disabled
    - **PASSED**

## ST5: Term Storage
### Acceptance Criteria:
- **Given** I have analyzed a sentence
**When** I click the Save Terms button
**Then** the system extracts terms (nouns, verbs, adjectives) from the input sentence and saves them on the server for future use
    - **PASSED**

- **Given** I have not analyzed a sentence
**Then** the Save Terms button is disabled
    - **PASSED**

## ST6: Time-Based Generation
### Acceptance Criteria:
- **Given** I have already analyzed a sentence
**When** I select a time tense (present, past, future) and click the Generate Sentence button
**Then** the system generates a sentence in the selected time tense and displaysis it
    - **PASSED**