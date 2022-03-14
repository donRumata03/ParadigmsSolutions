"""
There are three major repositories for GK's HTs:
	— Working repo
	— Tests: we can `pull origin master` and copy tests into working repo
		with rewriting and deleting all tests from it
	— Presenting repo: we can copy non-tests into it
		(and commit by hand afterwards)

This is achieved by marking some source files in Working repo as tests
and others — as MY solutions. Use ./.testignore for «ignoring» tests
with gitignore syntax inside
"""

import os
from typing import Iterable

import gitignore_parser
from pathlib import Path
from sys import argv
import colorama

from scripts.test_path_filterer import filter_tests, filter_out_tests

colorama.init(autoreset=True)

from scripts.location_detector import *


assert len(argv) >= 2
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
		# TODO: if COPIED test appears first time and doesn't match .testignore, add it there
		print(colorama.Fore.GREEN + "Updating tests…")

		for test_file in filter_tests(list_files(solutions_dir)):
			pass
			# print(f"Removing {test_file.relative_to(solutions_dir)}")
			# os.remove(str(test_file))
		print(colorama.Fore.GREEN + "Done updating tests!")
	case _:
		print(colorama.Fore.RED + "Unknown command!")
