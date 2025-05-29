# User Stories

## US1: Sentence Analysis
**As a** user
**I want to** insert a sentence
**So that** I can analyze it

### Description:
- System shall accept a sentence as input
- System shall validate the sentence structure
- System shall provide a syntactic tree visualization
- System shall extract and categorize:
  - Nouns
  - Verbs
  - Adjectives

### Acceptance Criteria:
- **Given** I have not selected the request syntactic tree option
**When** I input a sentence and click the Analyze Sentence button
**Then** the system processes the sentence and displays the Nouns, Verbs and Adjectives from the sentence

- **Given** I have selected the request syntactic tree option
**When** I input a sentence and click the Analyze Sentence button
**Then** the system processes the sentence, displays the Nouns, Verbs and Adjectives from the sentence and shows the syntactic tree visualization of the input sentence

- **Given** I have not inputed any sentence
**When** I click the Analyze Sentence button
**Then** the system displays a message requiring to input a sentence

#### Old Acceptance Criteria:
- User can input a sentence into the system through the input field in the UI.
- The system validates that the input is a well-formed sentence (not empty, contains at least a subject and a verb).
- If the sentence is invalid, the user receives a error message in the UI.
- The system extracts all nouns, verbs (categorized by tense: present, past, future), and adjectives from the sentence.
- The extracted terms are shown to the user in the UI.
- The syntactic tree visualization of the input sentence is shown, in the UI, if requested by the user before the sentence analysis is started.

## US2: Template Generation
**As a** user
**I want to** randomly generate a sentence template
**So that** I can understand how to build a sentence

### Description:
- System shall generate a random sentence template
- System shall display the template to the user

### Acceptance Criteria:
- **Given** I have already analyzed a sentence
**When** I click the Generate Template button
**Then** the system generates and displays a random sentence template with placeholders for nouns, verbs, adjectives and ohter sentences

-**Given** I have not analyzed a sentence
**Then** the Generate Template button is disabled

#### Old Acceptance Criteria:
- User can request the generation of a random sentence template.
- The system generates a sentence template with placeholders for nouns, verbs, and adjectives.
- The generated template is displayed to the user in the UI.
- The template structure is stored in the system while the user is connnected, ready for completion.

## US3: Template Completion
**As a** user  
**I want to** have the template to be completed
**So that** I can have a full sentence

### Description:
- System shall select appropriate terms from:
  - Built-in dictionary
  - Last analyzed input sentence
- System shall display the completed sentence

### Acceptance Criteria:
- **Given** I have already generated a sentence template
**When** I click the Generate Sentence button
**Then** the system fills the template with terms from its built-in dictionary and the input sentence and displays the completed sentence

- **Given** I have not generated a sentence template
**Then** the Generate Sentence button is disabled

#### Old Acceptance Criteria:
- User can request the completion of a sentence template.
- The system selects appropriate terms from the built-in dictionary and/or the analysis results of the last input sentences.
- The completed sentence is displayed to the user in the UI.
- The completed sentence is stored in the system while the user is connnected.

## US4: Toxicity Validation
**As a** user  
**I want to** validate the generated sentence
**So that** i can verify the toxicity

### Description:
- System shall analyze the sentence for toxicity
- System shall display the toxicity assessment results

### Acceptance Criteria:
- **Given** I have already generated a sentence
**When** I click the Validate Toxicity button
**Then** the system analyzes the sentence for toxicity and displays the results
- **Given** I have not generated a sentence
**Then** the Validate Toxicity button is disabled

#### Old Acceptance Criteria:
- User can request a toxicity analysis of the last generated sentence.
- The system analyzes the sentence for toxicity.
- The toxicity assessment results are displayed to the user, including a confidence score.

## US5: Term Storage
**As a** user  
**I want to** save the terms from the input sentence
**So that** they can be used in later templates

### Description:
- System shall extract terms from input sentences
- System shall store terms on the server
- System shall make stored terms available for future use

### Acceptance Criteria:
- **Given** I have analyzed a sentence
**When** I click the Save Terms button
**Then** the system extracts terms (nouns, verbs, adjectives) from the input sentence and saves them on the server for future use
- **Given** I have not analyzed a sentence
**Then** the Save Terms button is disabled

#### Old Acceptance Criteria:
- User can save extracted terms from the input sentence.
- The system stores the terms on the server for future use.
- If no terms were found the user receives a warning message in the UI, suggesting the user to analyze a more complete sentence.
- Stored terms are available for selection in future template completion.
- User receives confirmation when terms are successfully saved.

## US6: Time-Based Generation
**As a** user  
**I want to** specify the time tense for the template
**So that** I can have a sentence in a specific time tense

### Description:
- System shall allow selection of sentence time tense
- System shall generate sentences in the selected time tense
- System shall maintain grammatical consistency

### Acceptance Criteria:
- **Given** I have already analyzed a sentence
**When** I select a time tense (present, past, future) and click the Generate Sentence button
**Then** the system generates a sentence in the selected time tense and displaysis it

### Acceptance Criteria:
- User can select the desired time tense (present, past, future) for sentence generation.
- The system generates sentences in the selected time tense.
- Generated sentences maintain grammatical consistency with the chosen tense.
- The completed sentence, with the correct time tense, is displayed to the user in the UI.
- The completed sentence is stored in the system while the user is connnected.
