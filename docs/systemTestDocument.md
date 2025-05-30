---
title: System Test Document
layout: home
nav_order: 3
---

# System Test Document

## US1: Sentence Analysis
### AC01
**Given** I have not selected the request syntactic tree option
**When** I input a sentence and click the Analyze Sentence button
**Then** the system processes the sentence and displays the Nouns, Verbs and Adjectives from the sentence  
### AC02
**Given** I have selected the request syntactic tree option
**When** I input a sentence and click the Analyze Sentence button
**Then** the system processes the sentence, displays the Nouns, Verbs and Adjectives from the sentence and shows the syntactic tree visualization of the input sentence  

### AC03
**Given** I have not inputed any sentence
**When** I click the Analyze Sentence button
**Then** the system displays a message requiring to input a sentence    


## US2: Template Generation
### AC04
**Given** I have already analyzed a sentence
**When** I click the Generate Template button
**Then** the system generates and displays a random sentence template with placeholders for nouns, verbs, adjectives and ohter sentences

### AC05
**Given** I have not analyzed a sentence
**Then** the Generate Template button is disabled


## US3: Template Completion
### AC06
**Given** I have already generated a sentence template
**When** I click the Generate Sentence button
**Then** the system fills the template with terms from its built-in dictionary and the input sentence and displays the completed sentence

### AC07
**Given** I have not generated a sentence template
**Then** the Generate Sentence button is disabled


## US4: Toxicity Validation
### AC08
**Given** I have already generated a sentence
**When** I click the Validate Toxicity button
**Then** the system analyzes the sentence for toxicity and displays the results

### AC09
**Given** I have not generated a sentence
**Then** the Validate Toxicity button is disabled


## US5: Term Storage
### AC10
**Given** I have analyzed a sentence
**When** I click the Save Terms button
**Then** the system extracts terms (nouns, verbs, adjectives) from the input sentence and saves them on the server for future use

### AC11
**Given** I have not analyzed a sentence
**Then** the Save Terms button is disabled


## US6: Time-Based Generation
### AC12
**Given** I have already analyzed a sentence
**When** I select a time tense (present, past, future) and click the Generate Sentence button
**Then** the system generates a sentence in the selected time tense and displaysis it

# System Test Report
| Acceptance Criteria | Date | Status | Remarks |
|---------------------|------|--------|---------|
| AC01 | 30/05/2025 | OK | - |
| AC02 | 30/05/2025 | OK | - |
| AC03 | 30/05/2025 | OK | - |
| AC04 | 30/05/2025 | OK | - |
| AC05 | 30/05/2025 | OK | - |
| AC06 | 30/05/2025 | OK | - |
| AC07 | 30/05/2025 | OK | - |
| AC08 | 30/05/2025 | OK | - |
| AC09 | 30/05/2025 | OK | - |
| AC10 | 30/05/2025 | OK | - |
| AC11 | 30/05/2025 | OK | - |
| AC12 | 30/05/2025 | OK | - |
