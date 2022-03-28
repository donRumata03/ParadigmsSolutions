import gitignore_parser
from location_detector import solutions_dir
from scripts.test_path_filterer import test_detector


tested_paths = [
	("java/queue/QueueTest.java", True),
	("java/base/Either.java", True),
	("java/queue/Queues.java", True),                                # Special test

	("java/queue/QueueTestMy.java", False),                          # My test
	("java/search/DiscreteIterativeBinarySearch.java", False),       # Just code
]

for (f, ans) in tested_paths:
	p = solutions_dir / f
	assert p.exists()

	print(p, test_detector(p))
	assert test_detector(p) == ans

