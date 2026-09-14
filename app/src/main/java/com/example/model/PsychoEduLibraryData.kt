package com.example.model

object PsychoEduLibraryData {

    val cognitiveDistortions = listOf(
        CognitiveDistortion(
            id = "all_or_nothing",
            title = "All-or-Nothing Thinking",
            shortDescription = "Viewing situations in black-and-white categories. If performance falls short of perfect, you see yourself as a total failure.",
            example = "\"If I don't get an A on this paper, I'm completely incompetent.\"",
            socraticQuestion = "Is there a middle ground or spectrum between perfection and total failure?"
        ),
        CognitiveDistortion(
            id = "catastrophizing",
            title = "Catastrophizing",
            shortDescription = "Exaggerating the importance of negative events or expecting the worst possible outcome to happen.",
            example = "\"I stuttered during my presentation; now my career is completely ruined.\"",
            socraticQuestion = "What is the most realistic outcome, and how would I handle it if it did happen?"
        ),
        CognitiveDistortion(
            id = "overgeneralization",
            title = "Overgeneralization",
            shortDescription = "Viewing a single negative event as a never-ending pattern of defeat by using words like 'always' or 'never'.",
            example = "\"I didn't get this interview callback. I'll never find a job.\"",
            socraticQuestion = "Does one isolated event logically dictate the outcome of every future attempt?"
        ),
        CognitiveDistortion(
            id = "mental_filter",
            title = "Mental Filter",
            shortDescription = "Picking out a single negative detail and dwelling on it exclusively, darkening your vision of all reality.",
            example = "\"Out of 10 compliments, one colleague gave constructive criticism, so my whole work was terrible.\"",
            socraticQuestion = "What positive or neutral evidence am I filtering out of the bigger picture?"
        ),
        CognitiveDistortion(
            id = "mind_reading",
            title = "Mind Reading",
            shortDescription = "Arbitrarily concluding that someone is reacting negatively to you without checking it out.",
            example = "\"She looked away while I was speaking; she must think I'm boring and unintelligent.\"",
            socraticQuestion = "Do I have verifiable proof of what they are thinking, or could they just be tired or preoccupied?"
        ),
        CognitiveDistortion(
            id = "fortune_telling",
            title = "Fortune Telling",
            shortDescription = "Anticipating that things will turn out badly and feeling convinced that your prediction is an established fact.",
            example = "\"I already know I'm going to panic at the social gathering, so there's no point going.\"",
            socraticQuestion = "How many times in the past have my dire predictions failed to materialize?"
        ),
        CognitiveDistortion(
            id = "emotional_reasoning",
            title = "Emotional Reasoning",
            shortDescription = "Assuming that your negative emotions necessarily reflect objective reality: 'I feel it, therefore it must be true.'",
            example = "\"I feel anxious and overwhelmed, so something terrible must be about to happen.\"",
            socraticQuestion = "Are emotions facts, or are they physiological sensations influenced by fleeting thoughts?"
        ),
        CognitiveDistortion(
            id = "should_statements",
            title = "\"Should\" Statements",
            shortDescription = "Motivating yourself or others with 'shoulds' and 'musts', creating guilt, frustration, and resentment.",
            example = "\"I should never feel stressed. I must always be calm and productive.\"",
            socraticQuestion = "What happens if I replace 'I should' with 'I would prefer to' or 'It would be helpful if'?"
        ),
        CognitiveDistortion(
            id = "labeling",
            title = "Labeling",
            shortDescription = "An extreme form of overgeneralization where you attach a global, pejorative label to yourself or others.",
            example = "\"I made an arithmetic error. I'm such a born idiot.\"",
            socraticQuestion = "Is it more accurate to say 'I made a mistake' rather than defining my entire identity by that mistake?"
        ),
        CognitiveDistortion(
            id = "personalization",
            title = "Personalization & Blame",
            shortDescription = "Holding yourself personally responsible for an event that wasn't entirely under your control, or blaming others completely.",
            example = "\"My team's project deadline was missed; it is 100% my personal fault.\"",
            socraticQuestion = "What other external factors, circumstances, or people contributed to this outcome?"
        )
    )

    val dbtSkills = listOf(
        DbtSkill(
            id = "tipp",
            title = "TIPP Skills",
            category = "Distress Tolerance",
            subtitle = "Rapidly changing body chemistry when emotional arousal is high",
            explanation = "TIPP changes physiology to activate the parasympathetic nervous system (rest-and-digest), dropping extreme fight-or-flight heart rates.",
            actionableSteps = listOf(
                "T - Temperature: Hold an ice cube or splash cold water on your face while holding your breath for 15-30 seconds (triggers the mammalian dive reflex to drop heart rate).",
                "I - Intense Exercise: Engage in 2-5 minutes of high-intensity movement (jumping jacks, fast stairs, brisk walk) to burn off excess stress hormones.",
                "P - Paced Breathing: Slow your breathing to 5-6 breaths per minute. Inhale deeply for 4 seconds, exhale slowly for 6-7 seconds.",
                "P - Paired Muscle Relaxation: Tense muscle groups tightly while inhaling, then notice the release of tension as you breathe out, saying 'Relax'."
            ),
            tip = "Use TIPP when distress is 8/10 or higher and cognitive reasoning is temporarily offline."
        ),
        DbtSkill(
            id = "stop",
            title = "The STOP Skill",
            category = "Distress Tolerance",
            subtitle = "Preventing impulsive reactions that make painful situations worse",
            explanation = "STOP interrupts automatic emotional reflexes so you don't say or do things you regret.",
            actionableSteps = listOf(
                "S - Stop! Freeze. Do not move a muscle or react. Your emotions are pushing you to act without thinking.",
                "T - Take a step back. Take a deep breath. Physically step back from the situation or take a moment before replying.",
                "O - Observe. What is going on inside and outside? Notice thoughts, physical sensations, and facts of the environment without judging.",
                "P - Proceed Mindfully. Ask yourself: 'What action will make this situation better or worse? What does my Wise Mind say?'"
            ),
            tip = "Even taking a literal 10-second physical pause breaks the reactive impulse cycle."
        ),
        DbtSkill(
            id = "grounding_54321",
            title = "5-4-3-2-1 Sensory Grounding",
            category = "Mindfulness & Distress Tolerance",
            subtitle = "Pulling attention out of racing thoughts and anchoring into the present environment",
            explanation = "Engages all five sensory neural pathways to orient the brain back into physical safety.",
            actionableSteps = listOf(
                "5 things you can SEE: Notice subtle shapes, colors, reflections, or textures around you.",
                "4 things you can TOUCH: Feel your feet on the floor, texture of your clothes, cool desk, or your hands together.",
                "3 things you can HEAR: Listen for ambient sounds—air conditioning, birds, distant traffic, your own breathing.",
                "2 things you can SMELL: Notice the scent of your coffee, soap, fresh air, or fabric.",
                "1 thing you can TASTE: Notice the taste in your mouth, a sip of water, mint, or tea."
            ),
            tip = "Name the sensory items slowly and out loud if in a private space."
        ),
        DbtSkill(
            id = "opposite_action",
            title = "Opposite Action",
            category = "Emotion Regulation",
            subtitle = "Changing unwanted or unjustified emotional states by doing the exact opposite of the urge",
            explanation = "Emotions generate action urges (e.g. fear urges running away; sadness urges isolating). When the emotion doesn't fit the facts or isn't effective, doing the opposite action diminishes the emotion.",
            actionableSteps = listOf(
                "Identify the emotion and its natural urge (e.g. Fear -> urge to hide/avoid; Sadness -> urge to isolate and lie in bed; Anger -> urge to attack or yell).",
                "Check the facts: Is the emotion justified by the actual danger or situation? If not, execute opposite action.",
                "Opposite for Fear: Gently approach what you are avoiding. Keep head up and eye contact.",
                "Opposite for Sadness: Get active! Call a friend, take a walk, engage in a task.",
                "Opposite for Anger: Gently withdraw or practice kindness/empathy toward the person. Soften facial muscles."
            ),
            tip = "Do the opposite action all the way—with posture, facial expression, and tone of voice."
        ),
        DbtSkill(
            id = "wise_mind",
            title = "Wise Mind",
            category = "Mindfulness",
            subtitle = "The synthesis of Reasonable Mind and Emotional Mind",
            explanation = "Reasonable Mind is driven purely by cold facts and logic. Emotional Mind is driven by passionate feelings and urges. Wise Mind integrates both with deep inner wisdom.",
            actionableSteps = listOf(
                "Acknowledge Reasonable Mind: 'What are the objective data, logistics, and facts?'",
                "Acknowledge Emotional Mind: 'What feelings, needs, and bodily sensations are present?'",
                "Center in Wise Mind: Inhale deeply and focus on your center. Ask: 'What is the most balanced, compassionate, and effective path forward?'"
            ),
            tip = "Wise Mind feels calm, centered, and quiet compared to the urgency of emotional mind."
        )
    )

    val psychologyTopics = listOf(
        PsychologyTopic(
            id = "classical_conditioning",
            title = "Classical Conditioning",
            pioneer = "Ivan Pavlov (1890s)",
            category = "Behavioral",
            coreConcept = "A learning process that occurs when two stimuli are repeatedly paired: an unconditioned stimulus that produces an unconditioned response, and a neutral stimulus. Eventually, the neutral stimulus becomes a conditioned stimulus eliciting a conditioned response.",
            landmarkExperiment = "Pavlov paired the sound of a metronome/bell with the presentation of meat powder to dogs. Over repeated trials, the dogs began salivating to the sound alone, proving associative stimulus-response learning.",
            realWorldApplication = "Explains phobias, taste aversions, and environmental triggers in anxiety (e.g. a hospital smell triggering nausea after chemotherapy).",
            studyReflectionQuestion = "Can you identify a sound, scent, or notification tone that automatically triggers a physiological reaction in your daily life?"
        ),
        PsychologyTopic(
            id = "operant_conditioning",
            title = "Operant Conditioning",
            pioneer = "B.F. Skinner (1938)",
            category = "Behavioral",
            coreConcept = "Learning where behavior is controlled by consequences. Behaviors followed by reinforcement (positive or negative) increase in frequency; behaviors followed by punishment decrease in frequency.",
            landmarkExperiment = "The 'Skinner Box' contained a lever that a rat or pigeon pressed. When pressing the lever yielded a food pellet (positive reinforcement) or turned off an electric floor shock (negative reinforcement), lever-pressing increased dramatically. Variable ratio schedules produced highest persistence.",
            realWorldApplication = "Explains habit formation, video game reward loops, app notifications, and behavioral activation in CBT for depression.",
            studyReflectionQuestion = "How do unpredictable rewards (like social media notification algorithms) utilize variable-ratio reinforcement on our behavior?"
        ),
        PsychologyTopic(
            id = "cognitive_schemas",
            title = "Cognitive Schemas & Development",
            pioneer = "Jean Piaget (1936)",
            category = "Cognitive",
            coreConcept = "Schemas are mental building blocks or frameworks that organize and interpret information. We learn via Assimilation (fitting new information into existing schemas) and Accommodation (modifying schemas when new information contradicts them).",
            landmarkExperiment = "Piaget tested children with conservation tasks (e.g. pouring identical water into a tall thin beaker vs. wide beaker) to reveal that mental operations develop through distinct stages (Sensorimotor, Preoperational, Concrete, Formal Operational).",
            realWorldApplication = "Core foundation of CBT: Core beliefs act as cognitive schemas through which we filter and interpret all life events.",
            studyReflectionQuestion = "When was the last time you experienced cognitive accommodation—having to change a deeply held assumption about how the world works?"
        ),
        PsychologyTopic(
            id = "social_learning",
            title = "Social Learning & Self-Efficacy",
            pioneer = "Albert Bandura (1961, 1977)",
            category = "Social & Cognitive",
            coreConcept = "People learn through observational modeling and vicarious reinforcement, rather than only direct conditioning. Furthermore, 'Self-Efficacy'—one's belief in their ability to succeed—powerfully shapes motivation and performance.",
            landmarkExperiment = "The Bobo Doll Experiment: Children observed an adult aggressively punching and kicking an inflatable Bobo doll. When placed in the room later, children who witnessed the aggression reproduced the exact aggressive behaviors, proving observational learning without direct reward.",
            realWorldApplication = "In therapeutic modeling, exposure therapy, and building academic resilience through incremental mastery experiences.",
            studyReflectionQuestion = "Who has been a key positive behavioral model in your life, and what specific habits or coping styles did you adopt from observing them?"
        ),
        PsychologyTopic(
            id = "hierarchy_of_needs",
            title = "Hierarchy of Human Needs",
            pioneer = "Abraham Maslow (1943)",
            category = "Humanistic",
            coreConcept = "Human motivation is structured in a pyramid of five levels: Physiological needs (food, sleep), Safety, Love and Belonging, Esteem, and Self-Actualization. Deficit needs must generally be satisfied before higher growth needs emerge.",
            landmarkExperiment = "Maslow studied biographies and lives of psychologically healthy, high-achieving individuals (e.g., Einstein, Eleanor Roosevelt) to identify characteristics of self-actualization, such as peak experiences and acceptance of reality.",
            realWorldApplication = "In DBT's PLEASE skills: emotional regulation is virtually impossible if base biological needs (sleep, nutrition, illness) are neglected.",
            studyReflectionQuestion = "Which rung of the hierarchy feels most vulnerable or neglected when you experience stress?"
        ),
        PsychologyTopic(
            id = "cognitive_dissonance",
            title = "Cognitive Dissonance",
            pioneer = "Leon Festinger (1957)",
            category = "Cognitive",
            coreConcept = "When a person holds two contradictory beliefs, or their actions clash with their beliefs, they experience psychological distress (dissonance) and are driven to resolve it by altering thoughts, beliefs, or justifications.",
            landmarkExperiment = "Participants completed a painfully boring peg-turning task for an hour. They were paid either $1 or $20 to tell the next participant the task was fun. Those paid only $1 experienced severe dissonance and convinced themselves the task was actually enjoyable, whereas those paid $20 felt justified by the cash.",
            realWorldApplication = "Explains rationalization, political polarization, buyer's remorse, and why changing behavior often leads to changing attitude.",
            studyReflectionQuestion = "Have you ever found yourself defending a choice or habit simply to avoid the discomfort of admitting you made an error?"
        ),
        PsychologyTopic(
            id = "neuroplasticity_nervous_system",
            title = "The Polyvagal & Triune Brain Model",
            pioneer = "Paul MacLean & Stephen Porges",
            category = "Neuroscience",
            coreConcept = "The brain has evolved hierarchical defense systems: brainstem (reptilian / immobilization), limbic system (mammalian / amygdala fight-or-flight), and the neocortex (prefrontal cortex executive control). Stress triggers an 'amygdala hijack' that temporarily throttles prefrontal cognitive reasoning.",
            landmarkExperiment = "Modern fMRI imaging shows that deep paced diaphragmatic breathing and vagal nerve stimulation decrease amygdala activation and restore prefrontal blood flow within minutes.",
            realWorldApplication = "The physiological rationale for DBT's TIPP skills and CBT's somatic grounding: calm the autonomic nervous system first, then reframe thoughts.",
            studyReflectionQuestion = "How does understanding that emotional panic is a temporary biological reflex change your self-compassion during stressful moments?"
        )
    )
}
