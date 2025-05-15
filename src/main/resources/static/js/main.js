document.addEventListener('DOMContentLoaded', function() {
    const analyzeButton = document.getElementById('analyzeSentenceButton');
    const sentenceInput = document.getElementById('analyzeSentenceTextarea');
    const showAnalysisCheckbox = document.getElementById('showSentenceAnalysisCheckbox');
    const saveTermsCheckbox = document.getElementById('saveSentenceTermsCheckbox');
    const analysisText = document.getElementById('analysisCardText');
    const generateButton = document.getElementById('generateSentenceButton');
    const generatedTextArea = document.getElementById('generateSentenceTextarea');
    const toxicityAnalysisCheckbox = document.getElementById('showToxicityAnalysisCheckbox');
    const toxicityAnalysisCardText = document.getElementById('toxicityAnalysisCardText');

    analyzeButton.addEventListener('click', async function(e) {
        e.preventDefault();
        
        const sentence = sentenceInput.value.trim();
        if (!sentence) {
            alert('Please write a sentence first!');
            sentenceInput.focus();
            return;
        }

        try {
            analysisText.textContent = 'Analyzing...';
            analyzeButton.textContent = 'Analyzing...';

            const response = await fetch('/api/analyze', {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/plain'
                },
                body: sentence
            });
            
            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || 'Network response was not ok');
            }

            if (showAnalysisCheckbox.checked) {
                analysisText.textContent = result.data;
            } else {
                analysisText.textContent = 'Your sentence analysis will appear here';
            }
        } catch (error) {
            console.error('Error:', error);
            analysisText.textContent = error.message || 'Error analyzing sentence. Please try again.';
        }

        if (saveTermsCheckbox.checked) {
            try {
                const saveResponse = await fetch('/api/saveterms', {
                    method: 'POST'
                });

                const saveResult = await saveResponse.json();

                if (!saveResponse.ok) {
                    throw new Error(saveResult.message || 'Failed to save terms');
                }

                alert('Terms saved successfully!');
            } catch (error) {
                console.error('Error saving terms:', error);
                alert(error.message || 'Failed to save terms. Please try again.');
            }
        }
        
        analyzeButton.textContent = 'Analyze sentence';
    });

    generateButton.addEventListener('click', async function(e) {
        e.preventDefault();

        try {
            generateButton.textContent = 'Generating...';
            
            const response = await fetch('/api/generate?time=present', {
                method: 'GET'
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || 'Network response was not ok');
            }

            generatedTextArea.value = result.data;
            generateButton.textContent = 'Generate sentence';

            // After successful generation, check toxicity if enabled
            if(toxicityAnalysisCheckbox.checked) {
                try {
                    const toxicityResponse = await fetch('/api/toxicity', {
                        method: 'GET'
                    });

                    const toxicityResult = await toxicityResponse.json();

                    if (!toxicityResponse.ok) {
                        throw new Error(toxicityResult.message || 'Network response was not ok');
                    }

                    toxicityAnalysisCardText.textContent = toxicityResult.data;
                } catch (error) {
                    console.error('Error:', error);
                    toxicityAnalysisCardText.textContent = error.message || 'Error analyzing toxicity. Please try again.';
                }
            } else {
                toxicityAnalysisCardText.textContent = 'The toxicity analysis of the generated sentence will appear here';
            }
        } catch (error) {
            console.error('Error:', error);
            generatedTextArea.value = error.message || 'Error generating sentence. Please try again.';
            generateButton.textContent = 'Generate sentence';
        }
    });
}); 