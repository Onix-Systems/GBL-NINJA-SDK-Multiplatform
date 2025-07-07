#!/usr/bin/env python3
# tests/python-test/test.py

import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), '../../python-library'))

from gbl import Gbl, ParseResultSuccess, ParseResultFatal

def test_gbl_parsing():
    print('=== Python GBL Test ===')

    try:
        script_dir = os.path.dirname(os.path.abspath(__file__))
        file_path = os.path.join(script_dir, '../test.gbl')

        with open(file_path, 'rb') as f:
            file_data = f.read()

        print(f'Loaded test.gbl: {len(file_data)} bytes')

        gbl = Gbl()
        result = gbl.parse_byte_array(file_data)

        if isinstance(result, ParseResultSuccess):
            print(f'✓ Successfully parsed {len(result.result_list)} tags:')
            for i, tag in enumerate(result.result_list, 1):
                tag_name = tag.tag_type.name if hasattr(tag.tag_type, 'name') else str(tag.tag_type)
                print(f'  {i}. {tag_name}')
        else:
            print(f'✗ Parse failed: {result.error}')

    except FileNotFoundError:
        print('✗ Error: test.gbl file not found')
    except ImportError as e:
        print(f'✗ Error importing GBL library: {e}')
    except Exception as error:
        print(f'✗ Error: {error}')

if __name__ == '__main__':
    test_gbl_parsing()