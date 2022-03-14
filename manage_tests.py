import os
from typing import Iterable

import gitignore_parser
from pathlib import Path
from sys import argv
import colorama

from scripts.test_path_filterer import filter_tests, filter_out_tests

colorama.init(autoreset=True)

from scripts.location_detector import *


assert len(argv) == 2
match argv[1]:
	case "list-tests":
		print(colorama.Fore.GREEN + "These files are tests:")
		for test_file in filter_tests(list_files(solutions_dir)):
			print(test_file.relative_to(solutions_dir))
	case "list-non-tests":
		print(colorama.Fore.GREEN + "These files are NOT tests:")
		for test_file in filter_out_tests(list_files(solutions_dir)):
			print(test_file.relative_to(solutions_dir))
	case "delete-tests":
		print(colorama.Fore.GREEN + "Deleting tests…")
		for test_file in filter_tests(list_files(solutions_dir)):
			print(f"Removing {test_file.relative_to(solutions_dir)}")
			os.remove(str(test_file))
		print(colorama.Fore.GREEN + "Done deleting tests!")
	case "update-tests":
		print(colorama.Fore.GREEN + "Updating tests…")

		for test_file in filter_tests(list_files(solutions_dir)):
			pass
			# print(f"Removing {test_file.relative_to(solutions_dir)}")
			# os.remove(str(test_file))
		print(colorama.Fore.GREEN + "Done updating tests!")
	case _:
		print(colorama.Fore.RED + "Unknown command!")
