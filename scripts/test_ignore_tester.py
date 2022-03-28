import gitignore_parser
from location_detector import solutions_dir
from scripts.test_path_filterer import test_detector, non_presented_detector

tested_paths = [
	("java/queue/QueueTest.java", True, False),
	("java/base/Either.java", True, False),
	("java/queue/Queues.java", True, False), # Special test

	("java/queue/QueueTestMy.java", False, False), # My test
	("java/search/DiscreteIterativeBinarySearch.java", False, False), # Just code
	("java\\expression\\parser\\LL(1)_adapted_grammar.txt", False, False), # Grammar
	("java\\expression\\parser\\tests\\ParserTests.java", False, True), # NonPresented
]

for (f, test_ans, non_presented_ans) in tested_paths:
	p = solutions_dir / f
	assert p.exists()

	print(p, test_detector(p), non_presented_detector(p))
	assert test_detector(p) == test_ans and non_presented_detector(p) == non_presented_ans

